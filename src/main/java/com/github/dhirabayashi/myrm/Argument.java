package com.github.dhirabayashi.myrm;

import com.beust.jcommander.Parameter;

import java.util.List;

public class Argument {
    @Parameter(description = "files")
    List<String> files;
}
