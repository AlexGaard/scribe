package com.github.alexgaard.config;

import com.github.alexgaard.config.exception.MissingConfigValueException;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static com.github.alexgaard.config.ValueParser.parseUrl;

public class Config {

    private final Map<String, String> configMap;


    public Config(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public URL requireUrl(String name) {
        return getUrl(name).orElseThrow(() -> new MissingConfigValueException(name));
    }

    public Optional<URL> getUrl(String name) {
        return getString(name)
                .map(v -> parseUrl(name));
    }

    public Duration requireDuration(String name) {
        return getDuration(name)
                .orElseThrow(() -> new MissingConfigValueException(name));
    }

    public Optional<Duration> getDuration(String name) {
        return getString(name).map(Duration::parse);
    }

    public Optional<String> getString(String name) {
        return Optional.ofNullable(configMap.get(name));
    }


    public String requireString(String name) {
        return getString(name)
                .orElseThrow(() -> new MissingConfigValueException(name));
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

}
