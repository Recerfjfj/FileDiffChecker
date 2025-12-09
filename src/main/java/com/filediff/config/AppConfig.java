package com.filediff.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Класс для управления конфигурацией приложения через app.properties.
 */
public class AppConfig {
    private final Properties properties = new Properties();

    public AppConfig(String configPath) {
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Внимание: Файл конфигурации не найден (" + configPath + "). Используются настройки по умолчанию.");
        }
    }

    /**
     * Возвращает алгоритм хэширования.
     * @return строка алгоритма (MD5, SHA-1, SHA-256)
     */
    public String getHashAlgorithm() {
        return properties.getProperty("hash.algorithm", "MD5");
    }

    /**
     * Возвращает список паттернов файлов для игнорирования.
     * @return список расширений (напр. *.log)
     */
    public List<String> getIgnorePatterns() {
        String raw = properties.getProperty("scan.ignore.patterns", "");
        if (raw.isEmpty()) return Collections.emptyList();
        
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                // Преобразуем *.log в .log для простоты проверки окончания
                .map(p -> p.replace("*", "")) 
                .collect(Collectors.toList());
    }
}