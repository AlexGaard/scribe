package com.github.alexgaard.scribe.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.github.alexgaard.scribe.util.ExceptionUtil.soften;

public class FileUtils {

    public static Optional<String> getFileContentAsString(String filePath) {
        return toPath(filePath)
                .map(p -> soften(() -> Files.readString(p)));
    }

    public static Optional<byte[]> getFileContentAsBytes(String filePath) {
        return toPath(filePath)
                .map(p -> soften(() -> Files.readAllBytes(p)));
    }

    public static Optional<Path> toPath(String filePath) {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return Optional.empty();
        }

        return Optional.of(path);
    }

}
