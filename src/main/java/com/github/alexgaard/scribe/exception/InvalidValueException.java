package com.github.alexgaard.scribe.exception;

import java.util.List;

import static java.lang.String.format;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String name, String value, String reason) {
        super(format("Failed to parse \"%s\" = \"%s\". Error: %s", name, value, reason));
    }

    public InvalidValueException(String name, List<String> values, Throwable parseException) {
        super(format("Failed to parse \"%s\" = \"%s\". Error: %s", name, String.join(", ", values), parseException.getMessage()), parseException);
    }

    public InvalidValueException(String name, String value, Throwable parseException) {
        super(format("Failed to parse \"%s\" = \"%s\". Error: %s", name, value, parseException.getMessage()), parseException);
    }

}
