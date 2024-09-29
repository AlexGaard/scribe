package com.github.alexgaard.config.parser;

import com.github.alexgaard.config.exception.InvalidValueException;

public class ValueParser {

    public static Character parseCharacter(String key, String value) {
        if (value.length() != 1) {
            throw new InvalidValueException(key, value, "Expected 1 character in string, but found " + value.length());
        }

        return value.charAt(0);
    }

}
