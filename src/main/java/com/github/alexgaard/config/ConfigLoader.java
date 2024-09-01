package com.github.alexgaard.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

public class ConfigLoader {

    private final List<Supplier<Map<String, String>>> sources = new ArrayList<>();

    public ConfigLoader loadEnvironmentVariables() {
        return loadConfigMap(System.getenv());
    }

    public ConfigLoader loadSystemProperties() {
        return loadProperties(System.getProperties());
    }

    public ConfigLoader loadPropertiesFile(InputStream propertiesFileStream) {
        Properties properties = new Properties();

        try {
            properties.load(propertiesFileStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loadProperties(properties);
    }

    public ConfigLoader loadPropertiesFile(String filePath) {
        return this;
    }

    public ConfigLoader loadPropertiesFileIfExists(String filePath) {
        return this;
    }

    public ConfigLoader loadProperties(Properties properties) {
        sources.add(() -> {
            Map<String, String> values = new HashMap<>();
            properties.putAll(values);
            return values;
        });
        return this;
    }

    public ConfigLoader loadConfigMap(Map<String, String> configSource) {
        sources.add(() -> configSource);
        return this;
    }

    public ConfigLoader loadEnvFile(String filePath) {
        return loadConfigMap(EnvFileParser.parseEnvFileContent(filePath));
    }

    public ConfigLoader loadEnvFileIfExists(String filePath) {
        return loadConfigMap(EnvFileParser.parseEnvFileContent(filePath));
    }

    public ConfigLoader add(String name, String value) {
        sources.add(() -> Map.of(name, value));
        return this;
    }

    public Config merge() {
        Map<String, String> values = new HashMap<>();
        sources.forEach(s -> s.get().putAll(values));
        return new Config(values);
    }

}
