package com.github.alexgaard.config.exception;

public class MissingConfigValueException extends RuntimeException {

    public MissingConfigValueException(String key) {
        super("Missing config value " + key);
    }

}
