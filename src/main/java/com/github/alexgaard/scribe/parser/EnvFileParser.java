package com.github.alexgaard.scribe.parser;

import com.github.alexgaard.scribe.exception.InvalidEnvFileException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EnvFileParser {

    private EnvFileParser() {}

    private static final Pattern validNamePattern = Pattern.compile("[a-zA-Z_]+\\w*");

    public static Map<String, String> parseEnvFileContent(String envFileContent) {
        Map<String, String> entries = new HashMap<>();

        List<String> lines = envFileContent.lines().collect(Collectors.toList());

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank() || isLineComment(line)) {
                continue;
            }

            int seperator = line.indexOf('=');

            if (seperator < 0) {
                throw new InvalidEnvFileException(format("Invalid line: '%s'", line));
            }

            String name = line.substring(0, seperator).trim();
            
            if (!validNamePattern.matcher(name).matches()) {
                throw new InvalidEnvFileException(format("Invalid variable name '%s' in line: '%s'", name, line));
            }

            int valueStartIdx = seperator + 1;
            
            Character lineQuote = findLineQuote(line, valueStartIdx);

            if (lineQuote == null) {
                String value = getLineValue(line, valueStartIdx);
                entries.put(name, value);
                continue;
            }
            
            int quoteIdx = line.indexOf(lineQuote, valueStartIdx);
            
            int quoteEnd = findQuoteEnd(line, quoteIdx + 1, lineQuote);
            
            if (quoteEnd > 0) {
                String value = line.substring(quoteIdx + 1, quoteEnd);

                if (lineQuote == '"') {
                    value = handleDoubleQuoteExpansion(value);
                }

                entries.put(name, value);
                continue;
            }

            int lineEndIdx = -1;
            int lineEndQuoteIdx = -1;

            for (int j = i + 1; j < lines.size(); j++) {
                String nextLine = lines.get(j);

                int nextLineQuoteEnd = findQuoteEnd(nextLine, 0, lineQuote);

                if (nextLineQuoteEnd > 0) {
                    lineEndIdx = j;
                    lineEndQuoteIdx = nextLineQuoteEnd;
                    break;
                }
            }

            if (lineEndIdx < 0) {
                throw new InvalidEnvFileException(format("Variable '%s' is missing a quote to denote the end of the multi line value", name));
            }

            String multiLineValue = collectMultiLineString(lines, i, quoteIdx, lineEndIdx, lineEndQuoteIdx);

            if (lineQuote == '"') {
                multiLineValue = handleDoubleQuoteExpansion(multiLineValue);
            }

            entries.put(name, multiLineValue);

            i = lineEndIdx;
        }

        return entries;
    }

    private static String handleDoubleQuoteExpansion(String value) {
        return value
                .replace("\\\\t", "\t")
                .replace("\\\\r\\\\n", "\r\n")
                .replace("\\\\r", "\r")
                .replace("\\\\n", "\n");
    }

    private static String collectMultiLineString(List<String> lines, int startLineIdx, int startLineQuoteIdx, int endLineIdx, int endLineQuoteIdx) {
        StringBuilder builder = new StringBuilder();

        builder.append(lines.get(startLineIdx).substring(startLineQuoteIdx + 1))
                .append("\n");

        for (int i = startLineIdx + 1; i < endLineIdx; i++) {
            builder.append(lines.get(i)).append("\n");
        }

        builder.append(lines.get(endLineIdx), 0, endLineQuoteIdx);

        return builder.toString();
    }

    private static int findQuoteEnd(String line, int fromIdx, char quoteChar) {
        for (int i = fromIdx; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == quoteChar && isEscaped(line, i)) {
                return i;
            }
        }
        
        return -1;
    }

    private static boolean isEscaped(String line, int quoteIdx) {
        if (quoteIdx <= 0) {
            return false;
        }

        int firstNonSlashIdx = quoteIdx;

        for (int i = quoteIdx - 1; i > 0; i--) {
            char c = line.charAt(i);

            if (c != '\\') {
                firstNonSlashIdx = i;
                break;
            }
        }

        // odd number of slashes means that the quote is escaped, ex: \\\' and \\\\\' is an escaped quote while \\' is not
        return (quoteIdx - firstNonSlashIdx) % 2 != 0;
    }
    
    private static String getLineValue(String line, int fromIdx) {
        int commentIdx = line.indexOf('#', fromIdx);
        
        if (commentIdx < 0) {
            return line.substring(fromIdx).trim();
        }
        
        return line.substring(fromIdx, commentIdx).trim();
    }
    
    private static Character findLineQuote(String line, int fromIdx) {
        for (int i = fromIdx; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (Character.isWhitespace(c)) {
                continue;
            }
            
            if (c == '\'') {
                return '\'';
            }

            if (c == '"') {
                return '"';
            }
            
            return null;
        }
        
        return null;
    }

    private static boolean isLineComment(String line) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '_' || Character.isLetter(c)) {
                return false;
            }

            if (c == '#') {
                return true;
            }

            if (Character.isWhitespace(c)) {
                continue;
            }

            throw new InvalidEnvFileException(format("Invalid start of line: '%s'", line));
        }

        return false;
    }

}
