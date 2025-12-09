package com.filediff;

import com.filediff.config.AppConfig;
import com.filediff.model.FileSnapshot;
import com.filediff.service.DiffService;
import com.filediff.service.HashService;
import com.filediff.service.ScanService;
import com.filediff.util.JsonUtils;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Укажите режим работы: scan или diff");
            System.exit(1);
        }

        String mode = args[0];
        // Загружаем конфиг (предполагаем, что app.properties лежит в текущей директории запуска или resources)
        AppConfig config = new AppConfig("app.properties"); 

        try {
            if ("scan".equalsIgnoreCase(mode)) {
                runScan(config);
            } else if ("diff".equalsIgnoreCase(mode)) {
                runDiff(config);
            } else {
                System.err.println("Неизвестный режим: " + mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runScan(AppConfig config) throws Exception {
        String scanPath = System.getenv("SCAN_PATH");
        String output = System.getenv("SNAPSHOT_OUTPUT");

        if (scanPath == null || output == null) {
            throw new IllegalArgumentException("Переменные окружения SCAN_PATH и SNAPSHOT_OUTPUT обязательны для режима scan");
        }

        System.out.println("Запуск сканирования: " + scanPath);
        HashService hashService = new HashService(config.getHashAlgorithm());
        ScanService scanService = new ScanService(hashService, config.getIgnorePatterns());

        FileSnapshot snapshot = scanService.scan(scanPath);
        JsonUtils.writeSnapshot(snapshot, output);
        System.out.println("Snapshot сохранен в: " + output);
    }

    private static void runDiff(AppConfig config) throws Exception {
        String oldPath = System.getenv("SNAPSHOT_OLD");
        String newPath = System.getenv("SNAPSHOT_NEW");

        if (oldPath == null || newPath == null) {
            throw new IllegalArgumentException("Переменные окружения SNAPSHOT_OLD и SNAPSHOT_NEW обязательны для режима diff");
        }

        System.out.println("Сравнение snapshots...");
        FileSnapshot oldSnap = JsonUtils.readSnapshot(oldPath);
        FileSnapshot newSnap = JsonUtils.readSnapshot(newPath);

        DiffService diffService = new DiffService(config.getIgnorePatterns());
        diffService.compare(oldSnap, newSnap);
    }
}