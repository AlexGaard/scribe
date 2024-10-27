package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidFilePathException;
import com.github.alexgaard.scribe.exception.MissingValueException;
import com.github.alexgaard.scribe.parser.ValueParser;
import com.github.alexgaard.scribe.util.FileUtils;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.github.alexgaard.scribe.util.ExceptionUtil.wrapValueParsingException;
import static com.github.alexgaard.scribe.util.FileUtils.getFileContentAsString;

public class Config {

    private final Map<String, String> configMap;

    public Config(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public Optional<Boolean> getBoolean(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Boolean::parseBoolean));
    }

    public boolean requireBoolean(String name) {
        return getBoolean(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Integer> getInt(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Integer::parseInt));
    }

    public int requireInt(String name) {
        return getInt(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Long> getLong(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Long::parseLong));
    }

    public long requireLong(String name) {
        return getLong(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Short> getShort(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Short::parseShort));
    }

    public short requireShort(String name) {
        return getShort(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Float> getFloat(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Float::parseFloat));
    }

    public float requireFloat(String name) {
        return getFloat(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Double> getDouble(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Double::parseDouble));
    }

    public double requireDouble(String name) {
        return getDouble(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Character> getCharacter(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, ValueParser::parseCharacter));
    }

    public char requireCharacter(String name) {
        return getCharacter(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<URI> getUri(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, URI::new));
    }

    public URI requireUri(String name) {
        return getUri(name).orElseThrow(() -> new MissingValueException(name));
    }


    public Optional<URL> getUrl(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, URL::new));
    }

    public URL requireUrl(String name) {
        return getUrl(name).orElseThrow(() -> new MissingValueException(name));
    }


    public Optional<Duration> getDuration(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Duration::parse));
    }

    public Duration requireDuration(String name) {
        return getDuration(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Period> getPeriod(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Period::parse));
    }

    public Period requirePeriod(String name) {
        return getPeriod(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    /**
     * Retrieves the content of a file if it exists.
     * Expected config value example: "/path/to/file.txt"
     * @param name name of config variable
     * @return the content of the file if it exists
     */
    public Optional<String> getFileContent(String name) {
        return getString(name).flatMap(FileUtils::getFileContentAsString);
    }

    public String requireFileContent(String name) {
        String path = requireString(name);

        return getFileContentAsString(path)
                .orElseThrow(() -> new InvalidFilePathException(path));
    }

    public Optional<byte[]> getFileContentAsBytes(String name) {
        return getString(name).flatMap(FileUtils::getFileContentAsBytes);
    }

    public byte[] requireFileContentAsBytes(String name) {
        String path = requireString(name);

        return getFileContentAsBytes(path)
                .orElseThrow(() -> new InvalidFilePathException(path));
    }

    public Optional<Integer> getPortNumber(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, ValueParser::parsePortNumber));
    }

    public int requirePortNumber(String name) {
        return getPortNumber(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    /**
     * Retrieves a string list from the config with the given name if it exists.
     * The values in the list are separated by a comma, and whitespace is trimmed.
     * Ex: "foo ,  bar" will be parsed into List.of("foo", "bar")
     * @param name name of config variable
     * @return an optional list of strings
     */
    public Optional<List<String>> getStringList(String name) {
        return getString(name)
                .map(ValueParser::parseStringList);
    }

    public List<String> requireStringList(String name) {
        return getString(name)
                .map(ValueParser::parseStringList)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<String> getString(String name) {
        return Optional.ofNullable(configMap.get(name));
    }

    public String requireString(String name) {
        return getString(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    @Override
    public String toString() {
        return "Config{" +
                "configMapKeys=" + configMap.keySet() +
                '}';
    }

    public String toDetailedString() {
        return "Config{" +
                "configMap=" + configMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(configMap, config.configMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configMap);
    }
}
