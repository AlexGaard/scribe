package com.github.alexgaard.scribe.exception;

import static java.lang.String.format;

public class MissingValueException extends RuntimeException {

    public MissingValueException(String key) {
        super(format("Missing config value for key \"%s\"", key));
    }

}
