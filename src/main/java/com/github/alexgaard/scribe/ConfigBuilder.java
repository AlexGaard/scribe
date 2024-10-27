package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidFilePathException;
import com.github.alexgaard.scribe.parser.EnvFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.github.alexgaard.scribe.util.ExceptionUtils.soften;
import static com.github.alexgaard.scribe.util.FileUtils.getFileContentAsString;

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
            throw new InvalidFilePathException(filePath);
        }

        return this;
    }

    public ConfigBuilder loadPropertiesFileIfExists(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            log.debug("The properties file {} does not exist", filePath);
            loadPropertiesFile(filePath);
        }

        return this;
    }

    public ConfigBuilder loadProperties(Properties properties) {
        Map<String, String> values = properties.entrySet().stream().collect(
                Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> String.valueOf(e.getValue()),
                        (prev, next) -> next, HashMap::new
                ));

        configSources.add(() -> values);
        return this;
    }

    public ConfigBuilder loadConfigMap(Map<String, String> configSource) {
        configSources.add(() -> configSource);
        return this;
    }

    public ConfigBuilder loadEnvFile(String filePath) {
        String envFileContent = getFileContentAsString(filePath)
                .orElseThrow(() -> new InvalidFilePathException(filePath));

        return loadConfigMap(EnvFileParser.parseEnvFileContent(envFileContent));
    }

    public ConfigBuilder loadEnvFileIfExists(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            log.debug("The .env file {} does not exist", filePath);
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
