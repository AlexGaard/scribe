package com.github.alexgaard.scribe.exception;

import static java.lang.String.format;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String name, String value, String reason) {
        super(format("Failed to parse \"%s\" = \"%s\". Error: %s", name, value, reason));
    }

    public InvalidValueException(String name, String value, Throwable parseException) {
        super(format("Failed to parse \"%s\" = \"%s\". Error: %s", name, value, parseException.getMessage()), parseException);
    }

}
