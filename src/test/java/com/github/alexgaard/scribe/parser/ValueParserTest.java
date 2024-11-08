package com.github.alexgaard.scribe.parser;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.github.alexgaard.scribe.parser.ValueParser.parseDuration;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueParserTest {

    @Test
    void shouldParseDuration() {
        assertEquals(Duration.ofMinutes(5), parseDuration("5m"));
        assertEquals(Duration.ofSeconds(12), parseDuration("PT12s"));
        assertEquals(Duration.ofSeconds(12), parseDuration("pt12s"));
    }

}
