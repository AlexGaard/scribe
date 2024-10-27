package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidValueException;
import com.github.alexgaard.scribe.exception.MissingValueException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigTest {

    @Test
    public void shouldGetString() {
        Config config = configOf("foo", "bar");
        assertEquals("bar", config.getString("foo").get());
    }

    @Test
    public void shouldRequireString() {
        Config config = emptyConfig();
        assertThrows(MissingValueException.class, () -> config.requireString("foo1"));
    }

    @Test
    public void shouldGetPortNumber() {
        Config config = configOf("port_number", "42");

        assertEquals(42, config.getPortNumber("port_number").get());
    }

    @Test
    public void shouldRequirePortNumber() {
        Config config = emptyConfig();

        assertThrows(MissingValueException.class, () -> config.requirePortNumber("port_number_none"));
    }

    @Test
    public void shouldValidatePortNumber() {
        Config config1 = configOf("port_number_invalid", "-321");
        assertThrows(InvalidValueException.class, () -> config1.requirePortNumber("port_number_invalid"));


        Config config2 = configOf("port_number_invalid", "489231");
        assertThrows(InvalidValueException.class, () -> config2.requirePortNumber("port_number_invalid"));
    }

    @Test
    public void shouldGetCharacter() {
        Config config = configOf("char", "c");

        assertEquals('c', config.getCharacter("char").get());
    }

    @Test
    public void shouldValidateCharacter() {
        Config config = configOf("char", "abc");

        assertThrows(InvalidValueException.class, () -> config.getCharacter("char"));
    }

    @Test
    public void shouldGetStringList() {
        Config config = configOf("list", "foo,   bar");

        assertEquals(List.of("foo", "bar"), config.getStringList("list").get());
    }

    @Test
    public void shouldGetEmail() {
        Config config = configOf("email", "test@example.com");

        assertEquals("test@example.com", config.getEmail("email").get());
    }

    @Test
    public void shouldValidateEmail() {
        Config config = configOf("email", "test@.example.com");

        assertThrows(InvalidValueException.class, () -> config.getEmail("email").get());
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
