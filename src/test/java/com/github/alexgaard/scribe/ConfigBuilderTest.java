package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidFilePathException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigBuilderTest {

    @Test
    public void shouldLoadEnvVars() {
        Config config = new ConfigBuilder()
                .loadEnvironmentVariables()
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadSystemProps() {
        Config config = new ConfigBuilder()
                .loadSystemProperties()
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadConfigMap() {
        Config config = new ConfigBuilder()
                .loadConfigMap(Map.of("foo", "bar"))
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadProperties() {
        Properties properties = new Properties();
        properties.put("foo", "bar");

        Config config = new ConfigBuilder()
                .loadProperties(properties)
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadPropertiesFile() {
        Config config = new ConfigBuilder()
                .loadPropertiesFile("./data/test.properties")
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldThrowIfLoadingPropertiesFileThatDoesNotExist() {
        assertThrows(InvalidFilePathException.class, () -> {
            new ConfigBuilder()
                    .loadPropertiesFile("./data/does-not-exist.properties")
                    .build();
        });
    }

    @Test
    public void shouldLoadPropertiesFileIfExists() {
        Config config = new ConfigBuilder()
                .loadPropertiesFileIfExists("./data/does-not-exist.properties")
                .build();

        assertTrue(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadEnvFile() {
        Config config = new ConfigBuilder()
                .loadEnvFile("./data/test.env")
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldLoadEnvFileIfExists() {
        Config config = new ConfigBuilder()
                .loadEnvFileIfExists("./data/does-not-exist.env")
                .build();

        assertTrue(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldThrowIfLoadingEnvFileThatDoesNotExist() {
        assertThrows(InvalidFilePathException.class, () -> {
            new ConfigBuilder()
                    .loadEnvFile("./data/does-not-exist.properties")
                    .build();
        });
    }

    @Test
    public void shouldAddValues() {
        Config config = new ConfigBuilder()
                .add("foo", "bar")
                .build();

        assertFalse(config.getConfigMap().isEmpty());
    }

    @Test
    public void shouldOverwritePreviousConfig() {
        Config config = new ConfigBuilder()
                .add("foo", "bar")
                .add("foo", "baz")
                .build();

        assertEquals("baz", config.requireString("foo"));
    }

}
