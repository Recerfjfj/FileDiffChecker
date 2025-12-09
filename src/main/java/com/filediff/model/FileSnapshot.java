package com.filediff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Модель, представляющая snapshot файловой системы.
 */
public class FileSnapshot {
    @JsonProperty("root_path")
    private String rootPath;
    
    private List<FileInfo> files;

    public FileSnapshot() {}

    public FileSnapshot(String rootPath, List<FileInfo> files) {
        this.rootPath = rootPath;
        this.files = files;
    }

    public String getRootPath() { return rootPath; }
    public void setRootPath(String rootPath) { this.rootPath = rootPath; }

    public List<FileInfo> getFiles() { return files; }
    public void setFiles(List<FileInfo> files) { this.files = files; }
}