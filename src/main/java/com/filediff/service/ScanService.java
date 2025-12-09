package com.filediff.service;

import com.filediff.model.FileInfo;
import com.filediff.model.FileSnapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Сервис для сканирования директорий.
 */
public class ScanService {
    private final HashService hashService;
    private final List<String> ignoreSuffixes;

    public ScanService(HashService hashService, List<String> ignoreSuffixes) {
        this.hashService = hashService;
        this.ignoreSuffixes = ignoreSuffixes;
    }

    /**
     * Создает snapshot директории.
     * @param rootPath корневой путь для сканирования
     * @return объект FileSnapshot
     */
    public FileSnapshot scan(String rootPath) throws IOException {
        Path root = Paths.get(rootPath);
        if (!Files.exists(root) || !Files.isDirectory(root)) {
            throw new IllegalArgumentException("Путь не существует или не является директорией: " + rootPath);
        }

        List<FileInfo> fileInfos = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                  .forEach(path -> {
                      if (!shouldIgnore(path)) {
                          try {
                              // Получаем путь относительно корня (для переносимости)
                              String relativePath = root.relativize(path).toString().replace("\\", "/");
                              String hash = hashService.calculateHash(path);
                              fileInfos.add(new FileInfo(relativePath, hash));
                          } catch (Exception e) {
                              System.err.println("Ошибка обработки файла " + path + ": " + e.getMessage());
                          }
                      }
                  });
        }

        return new FileSnapshot(rootPath, fileInfos);
    }

    private boolean shouldIgnore(Path path) {
        String fileName = path.getFileName().toString();
        // Простое игнорирование конфигурации (app.properties) и gradlew по требованию
        if (fileName.equals("app.properties") || fileName.startsWith("gradlew")) {
            return true;
        }
        
        // Фильтрация по расширениям из конфига
        for (String suffix : ignoreSuffixes) {
            if (fileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}