package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidValueException;
import com.github.alexgaard.scribe.exception.MissingValueException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void shouldGetString() {
        Config config = configOf("foo", "bar");
        assertEquals("bar", config.getString("foo").get());
    }

    @Test
    void shouldRequireString() {
        Config config = emptyConfig();
        assertThrows(MissingValueException.class, () -> config.requireString("foo1"));
    }

    @Test
    void shouldGetPortNumber() {
        Config config = configOf("port_number", "42");

        assertEquals(42, config.getPortNumber("port_number").get());
    }

    @Test
    void shouldRequirePortNumber() {
        Config config = emptyConfig();

        assertThrows(MissingValueException.class, () -> config.requirePortNumber("port_number_none"));
    }

    @Test
    void shouldValidatePortNumber() {
        Config config1 = configOf("port_number_invalid", "-321");
        assertThrows(InvalidValueException.class, () -> config1.requirePortNumber("port_number_invalid"));


        Config config2 = configOf("port_number_invalid", "489231");
        assertThrows(InvalidValueException.class, () -> config2.requirePortNumber("port_number_invalid"));
    }

    @Test
    void shouldGetCharacter() {
        Config config = configOf("char", "c");

        assertEquals('c', config.getCharacter("char").get());
    }

    @Test
    void shouldValidateCharacter() {
        Config config = configOf("char", "abc");

        assertThrows(InvalidValueException.class, () -> config.getCharacter("char"));
    }

    @Test
    void shouldGetUuidList() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        Config config = configOf("uuids", String.join(", ", List.of(uuid1.toString(), uuid2.toString())));

        assertEquals(List.of(uuid1, uuid2), config.requireUuidList("uuids"));
    }


    @Test
    void shouldGetStringList() {
        Config config = configOf("list", "foo,   bar");

        assertEquals(List.of("foo", "bar"), config.getStringList("list").get());
    }

    @Test
    void shouldGetEmail() {
        Config config = configOf("email", "test@example.com");

        assertEquals("test@example.com", config.getEmail("email").get());
    }

    @Test
    void shouldValidateEmail() {
        Config config = configOf("email", "test@.example.com");

        assertThrows(InvalidValueException.class, () -> config.getEmail("email").get());
    }

    @Test
    void shouldCreateSubConfig() {
        Config config = new ConfigBuilder()
                .loadConfigMap(Map.of("prefix_val1", "foo", "prefix_val2", "bar", "baz", "val"))
                .build()
                .subConfig("prefix_", false);

        assertTrue(config.has("prefix_val1"));
        assertTrue(config.has("prefix_val2"));
        assertFalse(config.has("baz"));
    }

    @Test
    void shouldCreateSubConfigAndStripPrefix() {
        Config config = new ConfigBuilder()
                .loadConfigMap(Map.of("prefix_val1", "foo", "prefix_val2", "bar", "baz", "val"))
                .build()
                .subConfig("prefix_", true);

        assertTrue(config.has("val1"));
        assertTrue(config.has("val2"));
        assertFalse(config.has("baz"));
    }

    private Config emptyConfig() {
        return new ConfigBuilder()
                .build();
    }

    private static Config configOf(String name, String value) {
        return new ConfigBuilder()
                .add(name, value)
                .build();
    }

}
