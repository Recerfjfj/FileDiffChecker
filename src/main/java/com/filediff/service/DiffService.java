package com.filediff.service;

import com.filediff.model.FileInfo;
import com.filediff.model.FileSnapshot;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для сравнения двух snapshots.
 */
public class DiffService {
    private final List<String> ignoreSuffixes;

    public DiffService(List<String> ignoreSuffixes) {
        this.ignoreSuffixes = ignoreSuffixes;
    }

    /**
     * Сравнивает старый и новый snapshot и печатает результат.
     */
    public void compare(FileSnapshot oldSnap, FileSnapshot newSnap) {
        Map<String, String> oldMap = oldSnap.getFiles().stream()
                .filter(f -> !shouldIgnore(f.getPath()))
                .collect(Collectors.toMap(FileInfo::getPath, FileInfo::getHash));

        Map<String, String> newMap = newSnap.getFiles().stream()
                .filter(f -> !shouldIgnore(f.getPath()))
                .collect(Collectors.toMap(FileInfo::getPath, FileInfo::getHash));

        // Поиск CHANGED и ADDED
        for (Map.Entry<String, String> entry : newMap.entrySet()) {
            String path = entry.getKey();
            String newHash = entry.getValue();

            if (oldMap.containsKey(path)) {
                String oldHash = oldMap.get(path);
                if (!oldHash.equals(newHash)) {
                    System.out.println("CHANGED: " + path);
                }
            } else {
                System.out.println("ADDED: " + path);
            }
        }

        // Поиск REMOVED
        for (String path : oldMap.keySet()) {
            if (!newMap.containsKey(path)) {
                System.out.println("REMOVED: " + path);
            }
        }
    }

    private boolean shouldIgnore(String path) {
        for (String suffix : ignoreSuffixes) {
            if (path.endsWith(suffix)) return true;
        }
        return false;
    }
}