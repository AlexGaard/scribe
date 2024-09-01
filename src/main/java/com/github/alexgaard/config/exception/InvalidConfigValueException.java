package com.github.alexgaard.config.exception;

import static java.lang.String.format;

public class InvalidConfigValueException extends RuntimeException {

    public InvalidConfigValueException(String name, String value, String reason) {
        super(format("The config value \"%s\" is not valid. TODO %s %s", name, value, reason));
    }

}
