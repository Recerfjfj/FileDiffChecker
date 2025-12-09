package com.filediff.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Сервис для вычисления хэш-сумм файлов.
 */
public class HashService {
    private final String algorithm;

    public HashService(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Вычисляет хэш файла.
     * @param path путь к файлу
     * @return хэш в hex-формате
     */
    public String calculateHash(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}