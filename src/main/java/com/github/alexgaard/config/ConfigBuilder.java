package com.github.alexgaard.config;

import com.github.alexgaard.config.parser.EnvFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

import static com.github.alexgaard.config.util.ExceptionUtil.soften;

public class ConfigBuilder {

    private static final Logger log = LoggerFactory.getLogger(ConfigBuilder.class);

    private final List<Supplier<Map<String, String>>> configSources = new ArrayList<>();

    public ConfigBuilder loadEnvironmentVariables() {
        return loadConfigMap(System.getenv());
    }

    public ConfigBuilder loadSystemProperties() {
        return loadProperties(System.getProperties());
    }

    public ConfigBuilder loadPropertiesFile(InputStream propertiesFileStream) {
        Properties properties = new Properties();

        try {
            properties.load(propertiesFileStream);
        } catch (IOException e) {
            throw soften(e);
        }

        return loadProperties(properties);
    }

    public ConfigBuilder loadPropertiesFile(String filePath) {
        try {
            loadPropertiesFile(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw soften(e);
        }

        return this;
    }

    public ConfigBuilder loadPropertiesFileIfExists(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            log.info("The file {} does not exist. Skipping...", filePath);
            loadPropertiesFile(filePath);
        }

        return this;
    }

    public ConfigBuilder loadProperties(Properties properties) {
        configSources.add(() -> {
            Map<String, String> values = new HashMap<>();
            properties.putAll(values);
            return values;
        });
        return this;
    }

    public ConfigBuilder loadConfigMap(Map<String, String> configSource) {
        configSources.add(() -> configSource);
        return this;
    }

    public ConfigBuilder loadEnvFile(String filePath) {
        return loadConfigMap(EnvFileParser.parseEnvFileContent(filePath));
    }

    public ConfigBuilder loadEnvFileIfExists(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            log.info("The file {} does not exist. Skipping...", filePath);
            return loadConfigMap(EnvFileParser.parseEnvFileContent(filePath));
        }

        return this;
    }

    public ConfigBuilder add(String name, String value) {
        configSources.add(() -> Map.of(name, value));
        return this;
    }

    public Config build() {
        Map<String, String> values = new HashMap<>();
        configSources.forEach(s -> values.putAll(s.get()));
        return new Config(values);
    }

}
