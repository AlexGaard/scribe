package com.github.alexgaard.config;

import com.github.alexgaard.config.exception.MissingValueException;
import com.github.alexgaard.config.parser.ValueParser;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.github.alexgaard.config.exception.ExceptionUtil.wrapException;
import static com.github.alexgaard.config.parser.ValueParser.parseCharacter;

public class Config {

    private final Map<String, String> configMap;

    public Config(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public Optional<Boolean> getBoolean(String name) {
        return getString(name)
                .map(wrapException(name, Boolean::parseBoolean));
    }

    public Optional<Long> getLong(String name) {
        return getString(name)
                .map(wrapException(name, Long::parseLong));
    }

    public Optional<Short> getShort(String name) {
        return getString(name)
                .map(wrapException(name, Short::parseShort));
    }

    public Optional<Float> getFloat(String name) {
        return getString(name)
                .map(wrapException(name, Float::parseFloat));
    }

    public Optional<Double> getDouble(String name) {
        return getString(name)
                .map(wrapException(name, Double::parseDouble));
    }

    public Optional<Character> getCharacter(String name) {
        return getString(name)
                .map(wrapException(name, (v) -> parseCharacter(name, v)));
    }

    public URL requireUrl(String name) {
        return getUrl(name).orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<URL> getUrl(String name) {
        return getString(name)
                .map(wrapException(name, URL::new));
    }

    public Duration requireDuration(String name) {
        return getDuration(name)
                .orElseThrow(() -> new MissingValueException(name));
    }

    public Optional<Duration> getDuration(String name) {
        return getString(name)
                .map(wrapException(name, Duration::parse));
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
