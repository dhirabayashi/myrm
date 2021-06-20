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
        int ret = Main.rm(file);

        // verify
        assertFalse(Files.exists(file));
        assertEquals(0, ret);
    }

    @Test
    void rm_fileNotExists(@TempDir Path tempDir) throws IOException {
        // setup
        var file = tempDir.resolve("nonexistent");

        // run
        // 例外が投げられない
        int ret = Main.rm(file);

        // verify
        assertNotEquals(0, ret);
    }
}