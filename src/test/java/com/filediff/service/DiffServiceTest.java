package com.filediff.service;

import com.filediff.model.FileInfo;
import com.filediff.model.FileSnapshot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiffServiceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() { System.setOut(new PrintStream(outContent)); }

    @AfterEach
    public void restoreStreams() { System.setOut(originalOut); }

    @Test
    void testCompare() {
        FileSnapshot oldSnap = new FileSnapshot("/root", Arrays.asList(
                new FileInfo("a.txt", "111"),
                new FileInfo("b.txt", "222")
        ));
        FileSnapshot newSnap = new FileSnapshot("/root", Arrays.asList(
                new FileInfo("a.txt", "999"), // Changed
                new FileInfo("c.txt", "333")  // Added
                // b.txt Removed
        ));

        DiffService ds = new DiffService(Collections.emptyList());
        ds.compare(oldSnap, newSnap);

        String output = outContent.toString();
        assertTrue(output.contains("CHANGED: a.txt"));
        assertTrue(output.contains("REMOVED: b.txt"));
        assertTrue(output.contains("ADDED: c.txt"));
    }
}