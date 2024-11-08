package com.github.alexgaard.scribe.parser;

import java.time.Duration;
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

    public static Duration parseDuration(String value) {
        if (value.startsWith("PT") || value.startsWith("pt")) {
            return Duration.parse(value);
        } else {
            return Duration.parse("PT" + value);
        }
    }

    public static List<String> parseStringList(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
