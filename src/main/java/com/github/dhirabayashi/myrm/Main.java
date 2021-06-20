package com.github.dhirabayashi.myrm;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var argument = new Argument();
        var jCommander = JCommander.newBuilder()
                .addObject(argument)
                .build();
        jCommander.parse(args);

        if(argument.files == null || argument.help) {
            jCommander.usage();
            return;
        }

        // 終了コード
        int exitCode = 0;

        // 実行
        for(var arg : argument.files) {
            var file = Path.of(arg);
            int ret = rm(file);
            if(ret != 0) {
                exitCode = ret;
            }
        }

        System.exit(exitCode);
    }

    public static int rm(Path file) throws IOException {
        if(Files.exists(file)) {
            Files.delete(file);
            return 0;
        } else {
            System.err.printf("myrm: %s: No such file or director\n", file);
            return 1;
        }
    }
}
