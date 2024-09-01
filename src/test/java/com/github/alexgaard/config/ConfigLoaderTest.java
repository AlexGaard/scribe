package com.github.alexgaard.config;

import org.junit.jupiter.api.Test;

public class ConfigLoaderTest {

    @Test
    public void test() {
        Config config = new ConfigLoader()
                .loadEnvironmentVariables()
                .loadSystemProperties()
                .merge();

        System.out.println(config);
    }

}
