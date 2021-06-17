package com.github.dhirabayashi.myrm;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var argument = new Argument();
        JCommander.newBuilder()
                .addObject(argument)
                .build()
                .parse(args);

        // 実行
        for(var arg : argument.files) {
            var file = Path.of(arg);
            rm(file);
        }
    }

    public static void rm(Path file) throws IOException {
        Files.delete(file);
    }
}
