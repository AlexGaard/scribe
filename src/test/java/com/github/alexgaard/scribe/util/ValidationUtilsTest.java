package com.github.alexgaard.scribe.util;

import com.github.alexgaard.scribe.Config;
import com.github.alexgaard.scribe.ConfigBuilder;
import com.github.alexgaard.scribe.exception.InvalidValueException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.alexgaard.scribe.util.ValidationUtils.isEmailValid;
import static com.github.alexgaard.scribe.util.ValidationUtils.isValidPortNumber;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    void shouldValidatePortNumber() {
        assertFalse(isValidPortNumber(-1));
        assertTrue(isValidPortNumber(54654));
        assertFalse(isValidPortNumber(65536));
    }

    @Test
    void shouldValidateEmail() {
        assertFalse(isEmailValid("test@.example.com"));
        assertTrue(isEmailValid("test@example.com"));
        assertFalse(isEmailValid("test@a.com"));
        assertTrue(isEmailValid("43543543@example.com"));
    }

}
