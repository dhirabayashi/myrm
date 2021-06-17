package com.github.dhirabayashi.myrm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void rm(@TempDir Path tempDir) throws IOException {
        // setup
        var file = tempDir.resolve("test");
        Files.createFile(file);

        // run
        Main.rm(file);

        // verify
        assertFalse(Files.exists(file));
    }
}