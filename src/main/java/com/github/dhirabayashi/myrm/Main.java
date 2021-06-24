package com.github.dhirabayashi.myrm;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


        // オプションの設定
        List<Option> options = new ArrayList<>();
        if(argument.verbose) {
            options.add(Option.VERBOSE);
        }

        if(argument.deleteDirectories) {
            options.add(Option.DELETE_DIRECTORIES);
        }

        // 終了コード
        int exitCode = 0;

        // 実行
        var optionsArray = options.toArray(Option[]::new);
        for(var arg : argument.files) {
            var file = Path.of(arg);
            int ret = rm(file, optionsArray);
            if(ret != 0) {
                exitCode = ret;
            }
        }

        System.exit(exitCode);
    }

    public static int rm(Path file, Option... options) throws IOException {
        Set<Option> optionSet = Arrays.stream(options).collect(Collectors.toSet());

        if(Files.exists(file)) {
            if(!optionSet.contains(Option.DELETE_DIRECTORIES) && Files.isDirectory(file)) {
                System.err.printf("myrm: %s: is a directory\n", file);
                return 1;
            }

            if(Files.isDirectory(file) && !isEmptyDirectory(file)) {
                System.err.printf("myrm: %s: Directory not empty\n", file);
                return 1;
            }

            if(optionSet.contains(Option.VERBOSE)){
                System.out.println(file);
            }

            Files.delete(file);
            return 0;
        } else {
            System.err.printf("myrm: %s: No such file or director\n", file);
            return 1;
        }
    }

    private static boolean isEmptyDirectory(Path dir) throws IOException {
        try(var list = Files.list(dir)) {
            return list.count() == 0;
        }
    }
}
