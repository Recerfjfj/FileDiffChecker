package com.filediff.service;

import com.filediff.model.FileInfo;
import com.filediff.model.FileSnapshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScanServiceTest {

    @Test
    void testScan(@TempDir Path tempDir) throws Exception {
        // Подготовка структуры:
        // tempDir/
        //   file1.txt
        //   sub/
        //     file2.log (должен быть проигнорирован)
        //     file3.java
        
        Files.createFile(tempDir.resolve("file1.txt"));
        
        Path subDir = tempDir.resolve("sub");
        Files.createDirectories(subDir);
        Files.createFile(subDir.resolve("file2.log"));
        Files.createFile(subDir.resolve("file3.java"));

        // Настройка мока или реального сервиса хэширования
        HashService hashService = new HashService("MD5");
        
        // Игнорируем .log
        List<String> ignorePatterns = Arrays.asList(".log");
        ScanService scanService = new ScanService(hashService, ignorePatterns);

        // Действие
        FileSnapshot snapshot = scanService.scan(tempDir.toString());

        // Проверка
        assertNotNull(snapshot);
        assertEquals(tempDir.toString(), snapshot.getRootPath());
        
        List<FileInfo> files = snapshot.getFiles();
        // Ожидаем 2 файла (file1.txt и file3.java), file2.log игнорируем
        assertEquals(2, files.size());
        
        boolean hasFile1 = files.stream().anyMatch(f -> f.getPath().endsWith("file1.txt"));
        boolean hasFile3 = files.stream().anyMatch(f -> f.getPath().endsWith("file3.java"));
        boolean hasLog = files.stream().anyMatch(f -> f.getPath().endsWith("file2.log"));

        assertTrue(hasFile1, "file1.txt должен быть найден");
        assertTrue(hasFile3, "file3.java должен быть найден");
        assertFalse(hasLog, "file2.log должен быть проигнорирован");
    }
}