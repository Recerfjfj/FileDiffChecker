package com.filediff.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.filediff.model.FileSnapshot;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void writeSnapshot(FileSnapshot snapshot, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), snapshot);
    }

    public static FileSnapshot readSnapshot(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), FileSnapshot.class);
    }
}