package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidEnvFileException;
import com.github.alexgaard.scribe.parser.EnvFileParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static com.github.alexgaard.scribe.util.ExceptionUtils.soften;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnvFileParserTest {

    @Test
    void shouldParseEnvFile() {
        String envFileContent = readTestResource("example.env");
        Map<String, String> vars = EnvFileParser.parseEnvFileContent(envFileContent);

        assertEquals("basic", vars.get("BASIC"));
        assertEquals("AFTER_LINE", vars.get("AFTER_LINE"));
        assertEquals("", vars.get("EMPTY"));
        assertEquals("single_quotes", vars.get("SINGLE_QUOTES"));
        assertEquals("    single quotes    ", vars.get("SINGLE_QUOTES_SPACED"));
        assertEquals("double_quotes", vars.get("DOUBLE_QUOTES"));
        assertEquals("    double quotes    ", vars.get("DOUBLE_QUOTES_SPACED"));
        assertEquals("expand\nnew\nlines", vars.get("EXPAND_NEWLINES"));
        assertEquals("expand\nand\tand\r\nand\r", vars.get("EXPAND_MORE"));
        assertEquals("dontexpand\\nnewlines\\u1234", vars.get("DONT_EXPAND_UNQUOTED"));
        assertEquals("dontexpand\\nnewlines", vars.get("DONT_EXPAND_SQUOTED"));
        assertEquals("equals==", vars.get("EQUAL_SIGNS"));
        assertEquals("{\"foo\": \"bar\"}", vars.get("RETAIN_INNER_QUOTES"));
        assertEquals("{\"foo\": \"bar\"}", vars.get("RETAIN_INNER_QUOTES_AS_STRING"));
        assertEquals("some spaced out string", vars.get("TRIM_SPACE_FROM_UNQUOTED"));
        assertEquals("therealnerdybeast@example.tld", vars.get("USERNAME"));
        assertEquals("parsed", vars.get("SPACED_KEY"));
        assertEquals("foo#bar", vars.get("INLINE_COMMENT"));
        assertEquals("foo bar", vars.get("INLINE_COMMENT_PLAIN"));
        assertEquals("something\\\" # Comment", vars.get("END_BACKSLASH"));
        assertEquals("foo\\\\", vars.get("END_DOUBLE_BACKSLASH"));
        assertEquals("foo", vars.get("lowercased_var"));
        assertEquals("There are\n" +
                "many\\u1234lines\n" +
                "in this var!", vars.get("MULTILINE"));
        assertEquals("\n    <-- Note: Indentation preserved", vars.get("SPACED_MULTILINE"));
        assertEquals("Now also\n" +
                "with single quotes! \"_\"", vars.get("MULTILINE_SINGLE_QUOTE"));
        assertEquals("retained\"", vars.get("RETAIN_TRAILING_DQUOTE"));
        assertEquals("retained'", vars.get("RETAIN_TRAILING_SQUOTE"));
    }

    @Test
    void shouldNotHandleEscapedLineFeed() {
        String envFileContent = readTestResource("example.env");
        Map<String, String> vars = EnvFileParser.parseEnvFileContent(envFileContent);

        /*
        If the linefeed was escaped then it should be:
        "No linefeed --> \\nope"
         */
        assertEquals("No linefeed --> \\\n" +
                "nope", vars.get("MULTILINE_ESCAPED_LINEFEED"));
    }

    @Test
    void shouldThrowIfEnvFileIsInvalid() {
        String envFileContent = readTestResource("invalid.env");

        assertThrows(InvalidEnvFileException.class, () -> EnvFileParser.parseEnvFileContent(envFileContent));
    }


    private static String readTestResource(String fileName) {
        try (InputStream inputStream = EnvFileParserTest.class.getClassLoader().getResourceAsStream(fileName)) {
            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            throw soften(e);
        }
    }

}
