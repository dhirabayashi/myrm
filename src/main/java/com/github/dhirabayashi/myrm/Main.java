package com.github.dhirabayashi.myrm;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.io.UncheckedIOException;
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

        if(argument.recurse) {
            options.add(Option.DELETE_RECURSIVE);
        }

        if(argument.force) {
            options.add(Option.FORCE);
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
            if(Files.isDirectory(file) && !hasDirectoryRemoveOption(optionSet)) {
                System.err.printf("myrm: %s: is a directory\n", file);
                return 1;
            }

            if(Files.isDirectory(file) && !isEmptyDirectory(file) && !optionSet.contains(Option.DELETE_RECURSIVE)) {
                System.err.printf("myrm: %s: Directory not empty\n", file);
                return 1;
            }

            if(optionSet.contains(Option.DELETE_RECURSIVE)) {
                deleteAll(file, optionSet);
            } else {
                if(optionSet.contains(Option.VERBOSE)){
                    System.out.println(file);
                }

                Files.delete(file);
            }
        } else if(!optionSet.contains(Option.FORCE))  {
            System.err.printf("myrm: %s: No such file or director\n", file);
            return 1;
        }
        return 0;
    }

    private static boolean hasDirectoryRemoveOption(Set<Option> options) {
        return options.contains(Option.DELETE_DIRECTORIES) || options.contains(Option.DELETE_RECURSIVE);
    }

    private static boolean isEmptyDirectory(Path dir) throws IOException {
        try(var list = Files.list(dir)) {
            return list.count() == 0;
        }
    }

    private static void deleteAll(Path path, Set<Option> options) throws IOException {
        if(Files.isDirectory(path)) {
            try(var list = Files.list(path)) {
                list.forEach(target -> {
                    try {
                        deleteAll(target, options);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        }
        delete(path, options);
    }

    private static void delete(Path path, Set<Option> options) throws IOException {
        if(options.contains(Option.VERBOSE)) {
            System.out.println(path);
        }
        Files.delete(path);
    }
}
