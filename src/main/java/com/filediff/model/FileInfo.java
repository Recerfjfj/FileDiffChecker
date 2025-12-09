package com.filediff.model;

/**
 * Модель, представляющая информацию об отдельном файле.
 */
public class FileInfo {
    private String path;
    private String hash;

    public FileInfo() {} // Нужен для Jackson

    public FileInfo(String path, String hash) {
        this.path = path;
        this.hash = hash;
    }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}