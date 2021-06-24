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

    @Test
    void rm_directory(@TempDir Path tempDir) throws IOException {
        // setup
        var dir = tempDir.resolve("test");
        Files.createDirectory(dir);

        // run
        int ret = Main.rm(dir);

        // verify
        assertNotEquals(0, ret);
        // 消えない
        assertTrue(Files.exists(dir));
    }

    @Test
    void rm_deleteDirectory(@TempDir Path tempDir) throws IOException {
        // setup
        var dir = tempDir.resolve("test");
        Files.createDirectory(dir);

        // run
        int ret = Main.rm(dir, Option.DELETE_DIRECTORIES);

        // verify
        assertEquals(0, ret);
        // オプションがあれば消える
        assertFalse(Files.exists(dir));
    }

    @Test
    void rm_notEmptyDirectory(@TempDir Path tempDir) throws IOException {
        // setup
        var dir = tempDir.resolve("test");
        Files.createDirectory(dir);
        Files.createFile(dir.resolve("file"));

        // run
        int ret = Main.rm(dir, Option.DELETE_DIRECTORIES);

        // verify
        assertNotEquals(0, ret);
        // 空でないので消えない
        assertTrue(Files.exists(dir));
    }
}