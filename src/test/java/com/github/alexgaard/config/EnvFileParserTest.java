package com.github.alexgaard.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.internal.DotenvParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static com.github.alexgaard.config.exception.ExceptionUtil.soften;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnvFileParserTest {

    @Test
    void shouldParseValidEnvFile() {
        String envFileContent = readTestResource("valid.env");
        Map<String, String> envVariables = EnvFileParser.parseEnvFileContent(envFileContent);
        assertEquals("world", envVariables.get("HELLO"));
        assertEquals("test", envVariables.get("VAR_1"));
        assertEquals("test1", envVariables.get("VAR_2"));
        assertEquals("=", envVariables.get("VAR_3"));
        assertEquals("test1#test2", envVariables.get("VAR_4"));
        assertEquals("test5\"", envVariables.get("var_5"));

        Dotenv dotenv = Dotenv.configure()
                .filename("valid.env")
                .load();

        envVariables.forEach((name, value) -> {
            assertEquals(dotenv.get(name), value);
        });
    }

    private static String readTestResource(String fileName) {
        try (InputStream inputStream = EnvFileParserTest.class.getClassLoader().getResourceAsStream(fileName)) {
            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            throw soften(e);
        }
    }

}
