package com.github.alexgaard.scribe.exception;

public class InvalidFilePathException extends RuntimeException {

    public InvalidFilePathException(String filePath) {
        super("Unable to find a file at path: " + filePath);
    }

}
