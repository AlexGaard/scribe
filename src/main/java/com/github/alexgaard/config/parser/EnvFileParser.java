package com.github.alexgaard.config.parser;

import com.github.alexgaard.config.exception.InvalidEnvFileException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvFileParser {

    private final static Pattern validNamePattern = Pattern.compile("[a-zA-Z_]+[a-zA-Z0-9_]*");

    public static Map<String, String> parseEnvFileContent(String envFileContent) {
        List<EnvEntry> envEntries = new ArrayList<>();

        AtomicReference<EnvEntry> currentEntry = new AtomicReference<>();

        envFileContent.lines().forEach(line -> {
            EnvEntry envEntry = currentEntry.get();

            if (envEntry != null) {
                envEntry.value += "\n" + line;
            }

            if (line.isEmpty() || line.charAt(0) == '#') {
                return;
            }

            String trimmedLine = line.trim();

            if (trimmedLine.isBlank()) {
                return;
            }

            if (envEntry == null) {
                int separatorIdx = trimmedLine.indexOf("=");

                if (separatorIdx == -1) {
                    throw new InvalidEnvFileException();
                }

                String name = trimmedLine.substring(0, separatorIdx).trim();
                Matcher matcher = validNamePattern.matcher(name);

                if (!matcher.matches()) {
                    throw new InvalidEnvFileException();
                }

                String value = trimmedLine.substring(separatorIdx + 1).trim();

                boolean isQuoted = value.charAt(0) == '"';
                boolean hasEndQuote = value.charAt(value.length() - 1) == '"';

                if (!isQuoted || hasEndQuote) {
                    envEntries.add(new EnvEntry(name, trimQuotes(value)));
                } else {
                    currentEntry.set(new EnvEntry(name, value));
                }
            } else {
                boolean hasEndQuote = trimmedLine.charAt(trimmedLine.length() - 1) == '"';

                if (hasEndQuote) {
                    envEntries.add(new EnvEntry(envEntry.name, trimQuotes(envEntry.value)));
                    currentEntry.set(null);
                }
            }
        });

        EnvEntry envEntry = currentEntry.get();

        if (envEntry != null) {
            throw new InvalidEnvFileException();
        }

        Map<String, String> entries = new HashMap<>();
        envEntries.forEach(e -> entries.put(e.name, e.value));

        return entries;
    }


    private static String trimQuotes(String str) {
        if (str.isEmpty()) {
            return str;
        }

        if (str.charAt(0) == '"') {
            int endIdx = str.lastIndexOf("\"");
            return str.substring(1, endIdx);
        }

        return str;
    }

    private static class EnvEntry {
        public String name;
        public String value;

        public EnvEntry(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

}
