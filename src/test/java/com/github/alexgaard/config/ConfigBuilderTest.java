package com.github.alexgaard.config;

import org.junit.jupiter.api.Test;

public class ConfigBuilderTest {

    @Test
    public void test() {
        Config config = new ConfigBuilder()
                .loadEnvironmentVariables()
                .loadSystemProperties()
                .build();

        System.out.println(config);
    }

}
