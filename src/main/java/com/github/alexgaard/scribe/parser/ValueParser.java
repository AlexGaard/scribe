package com.github.alexgaard.scribe.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValueParser {

    private ValueParser() {}

    public static Character parseCharacter(String value) {
        if (value.length() != 1) {
            throw new IllegalArgumentException("Expected 1 character in string, but found " + value.length());
        }

        return value.charAt(0);
    }

    public static int parsePortNumber(String value) {
        int val = Integer.parseInt(value);

        if (val < 0 || val > 65535) {
            throw new IllegalArgumentException(value + " is not a valid port number");
        }

        return val;
    }

    public static List<String> parseStringList(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
