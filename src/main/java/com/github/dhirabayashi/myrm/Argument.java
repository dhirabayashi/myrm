package com.github.dhirabayashi.myrm;

import com.beust.jcommander.Parameter;

import java.util.List;

public class Argument {
    @Parameter(description = "files")
    List<String> files;

    @Parameter(names = {"-h", "-help"}, description = "help")
    boolean help;

    @Parameter(names = "-v", description = "Be verbose")
    boolean verbose;

    @Parameter(names = "-d", description = "Attempt to remove directories.")
    boolean deleteDirectories;
}
