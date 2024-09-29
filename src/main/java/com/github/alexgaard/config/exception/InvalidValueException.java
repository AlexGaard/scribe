package com.github.alexgaard.config.exception;

import static java.lang.String.format;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String key, String value, String reason) {
        super(format("Failed to parse the value \"%s\" for key \"%s\". %s", key, value, reason));
    }

    public InvalidValueException(String key, String value, Throwable parseException) {
        super(format("Failed to parse the value \"%s\" for key \"%s\". Error: %s", key, value, parseException.getMessage()), parseException);
    }

}
