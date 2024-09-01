package com.github.alexgaard.config;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.alexgaard.config.exception.ExceptionUtil.soften;

public class ValueParser {

    public static URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw soften(e);
//            throw new InvalidConfigValueException(name, v, "not valid URL");
        }
    }

}
