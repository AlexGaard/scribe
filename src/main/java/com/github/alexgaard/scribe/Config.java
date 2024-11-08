package com.github.alexgaard.scribe;

import com.github.alexgaard.scribe.exception.InvalidFilePathException;
import com.github.alexgaard.scribe.exception.InvalidValueException;
import com.github.alexgaard.scribe.exception.MissingValueException;
import com.github.alexgaard.scribe.parser.ValueParser;
import com.github.alexgaard.scribe.util.ConfigUtils;
import com.github.alexgaard.scribe.util.FileUtils;

import java.net.URI;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.alexgaard.scribe.util.ExceptionUtils.wrapValueParsingException;
import static com.github.alexgaard.scribe.util.ExceptionUtils.wrapValuesParsingException;
import static com.github.alexgaard.scribe.util.FileUtils.getFileContentAsString;
import static com.github.alexgaard.scribe.util.ValidationUtils.isEmailValid;
import static com.github.alexgaard.scribe.util.ValidationUtils.isValidPortNumber;

public class Config {

    private final Map<String, String> configMap;

    public Config(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public Optional<Boolean> getBoolean(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Boolean::parseBoolean));
    }

    public boolean requireBoolean(String name) {
        return getBoolean(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Boolean>> getBooleanList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Boolean::parseBoolean));
    }

    public List<Boolean> requireBooleanList(String name) {
        return getBooleanList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Integer> getInt(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Integer::parseInt));
    }

    public int requireInt(String name) {
        return getInt(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Integer>> getIntList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Integer::parseInt));
    }

    public List<Integer> requireIntList(String name) {
        return getIntList(name)
                .orElseThrow(missingValue(name));
    }
    
    public Optional<Long> getLong(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Long::parseLong));
    }

    public long requireLong(String name) {
        return getLong(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Long>> getLongList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Long::parseLong));
    }

    public List<Long> requireLongList(String name) {
        return getLongList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Short> getShort(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Short::parseShort));
    }

    public short requireShort(String name) {
        return getShort(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Short>> getShortList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Short::parseShort));
    }

    public List<Short> requireShortList(String name) {
        return getShortList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Float> getFloat(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Float::parseFloat));
    }

    public float requireFloat(String name) {
        return getFloat(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Float>> getFloatList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Float::parseFloat));
    }

    public List<Float> requireFloatList(String name) {
        return getFloatList(name)
                .orElseThrow(missingValue(name));
    }


    public Optional<Double> getDouble(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Double::parseDouble));
    }

    public double requireDouble(String name) {
        return getDouble(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Double>> getDoubleList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Double::parseDouble));
    }

    public List<Double> requireDoubleList(String name) {
        return getDoubleList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Character> getCharacter(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, ValueParser::parseCharacter));
    }

    public char requireCharacter(String name) {
        return getCharacter(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Character>> getCharacterList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, ValueParser::parseCharacter));
    }

    public List<Character> requireCharacterList(String name) {
        return getCharacterList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<UUID> getUuid(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, UUID::fromString));
    }

    public UUID requireUuid(String name) {
        return getUuid(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<UUID>> getUuidList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, UUID::fromString));
    }

    public List<UUID> requireUuidList(String name) {
        return getUuidList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<LocalTime> getLocalTime(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, LocalTime::parse));
    }

    public LocalTime requireLocalTime(String name) {
        return getLocalTime(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<LocalTime>> getLocalTimeList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, LocalTime::parse));
    }

    public List<LocalTime> requireLocalTimeList(String name) {
        return getLocalTimeList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<LocalDate> getLocalDate(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, LocalDate::parse));
    }

    public LocalDate requireLocalDate(String name) {
        return getLocalDate(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<LocalDate>> getLocalDateList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, LocalDate::parse));
    }

    public List<LocalDate> requireLocalDateList(String name) {
        return getLocalDateList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<LocalDateTime> getLocalDateTime(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, LocalDateTime::parse));
    }

    public LocalDateTime requireLocalDateTime(String name) {
        return getLocalDateTime(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<LocalDateTime>> getLocalDateTimeList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, LocalDateTime::parse));
    }

    public List<LocalDateTime> requireLocalDateTimeList(String name) {
        return getLocalDateTimeList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<OffsetDateTime> getOffsetDateTime(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, OffsetDateTime::parse));
    }

    public OffsetDateTime requireOffsetDateTime(String name) {
        return getOffsetDateTime(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<OffsetDateTime>> getOffsetDateTimeList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, OffsetDateTime::parse));
    }

    public List<OffsetDateTime> requireOffsetDateTimeList(String name) {
        return getOffsetDateTimeList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<ZonedDateTime> getZonedDateTime(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, ZonedDateTime::parse));
    }

    public ZonedDateTime requireZonedDateTime(String name) {
        return getZonedDateTime(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<ZonedDateTime>> getZonedDateTimeList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, ZonedDateTime::parse));
    }

    public List<ZonedDateTime> requireZonedDateTimeList(String name) {
        return getZonedDateTimeList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<URI> getUri(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, URI::new));
    }

    public URI requireUri(String name) {
        return getUri(name).orElseThrow(missingValue(name));
    }

    public Optional<List<URI>> getUriList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, URI::new));
    }

    public List<URI> requireUriList(String name) {
        return getUriList(name)
                .orElseThrow(missingValue(name));
    }


    public Optional<URL> getUrl(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, URL::new));
    }

    public URL requireUrl(String name) {
        return getUrl(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<URL>> getUrlList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, URL::new));
    }

    public List<URL> requireUrlList(String name) {
        return getUrlList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Duration> getDuration(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, ValueParser::parseDuration));
    }

    public Duration requireDuration(String name) {
        return getDuration(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Duration>> getDurationList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, ValueParser::parseDuration));
    }

    public List<Duration> requireDurationList(String name) {
        return getDurationList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Period> getPeriod(String name) {
        return getString(name)
                .map(wrapValueParsingException(name, Period::parse));
    }

    public Period requirePeriod(String name) {
        return getPeriod(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Period>> getPeriodList(String name) {
        return getStringList(name)
                .map(wrapValuesParsingException(name, Period::parse));
    }

    public List<Period> requirePeriodList(String name) {
        return getPeriodList(name)
                .orElseThrow(missingValue(name));
    }

    /**
     * Retrieves the content of a file if it exists.
     * Expected config value example: "/path/to/file.txt"
     *
     * @param name name of config variable
     * @return the content of the file if it exists
     */
    public Optional<String> getFileContent(String name) {
        return getString(name)
                .flatMap(FileUtils::getFileContentAsString);
    }

    public String requireFileContent(String name) {
        String path = requireString(name);

        return getFileContentAsString(path)
                .orElseThrow(() -> new InvalidFilePathException(path));
    }

    /**
     * Retrieves the content of a file if it exists.
     * Expected config value example: "/path/to/file.txt"
     *
     * @param name name of config variable
     * @return the content of the file if it exists
     */
    public Optional<byte[]> getFileContentAsBytes(String name) {
        return getString(name).flatMap(FileUtils::getFileContentAsBytes);
    }

    public byte[] requireFileContentAsBytes(String name) {
        String path = requireString(name);

        return getFileContentAsBytes(path)
                .orElseThrow(() -> new InvalidFilePathException(path));
    }

    public Optional<String> getEmail(String name) {
        return getString(name)
                .map(email -> {
                    if (!isEmailValid(email)) {
                        throw new InvalidValueException(name, email, "Email is invalid");
                    }

                    return email;
                });
    }

    public String requireEmail(String name) {
        return getEmail(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<String>> getEmailList(String name) {
        return getStringList(name)
                .map(emails -> {
                    emails.forEach(e -> {
                        if (!isEmailValid(e)) {
                            throw new InvalidValueException(name, e, "Email is invalid");
                        }
                    });

                    return emails;
                });
    }

    public List<String> requireEmailList(String name) {
        return getEmailList(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<Integer> getPortNumber(String name) {
        return getInt(name)
                .map(portNumber -> {
                    if (!isValidPortNumber(portNumber)) {
                        throw new InvalidValueException(name, portNumber.toString(), "Port number is invalid");
                    }

                    return portNumber;
                });
    }

    public int requirePortNumber(String name) {
        return getPortNumber(name)
                .orElseThrow(missingValue(name));
    }

    public Optional<List<Integer>> getPortNumberList(String name) {
        return getIntList(name)
                .map(portNumbers -> {
                    portNumbers.forEach(portNumber -> {
                        if (!isValidPortNumber(portNumber)) {
                            throw new InvalidValueException(name, portNumber.toString(), "Port number is invalid");
                        }
                    });

                    return portNumbers;
                });
    }

    public List<Integer> requirePortNumberList(String name) {
        return getPortNumberList(name)
                .orElseThrow(missingValue(name));
    }

    /**
     * Retrieves a string list from the config with the given name if it exists.
     * The values in the list are separated by a comma, and whitespace is trimmed.
     * Ex: "foo ,  bar" will be parsed into List.of("foo", "bar")
     *
     * @param name name of config variable
     * @return an optional list of strings
     */
    public Optional<List<String>> getStringList(String name) {
        return getString(name)
                .map(ValueParser::parseStringList);
    }

    public List<String> requireStringList(String name) {
        return getString(name)
                .map(ValueParser::parseStringList)
                .orElseThrow(missingValue(name));
    }

    public Optional<String> getString(String name) {
        return Optional.ofNullable(configMap.get(name));
    }

    public String requireString(String name) {
        return getString(name)
                .orElseThrow(missingValue(name));
    }

    public boolean has(String name) {
        return configMap.containsKey(name);
    }

    /**
     * Creates a new config where all the names starts with the given prefix.
     *
     * @param namePrefix  the prefix used to filter values to be included in the sub config
     * @param stripPrefix if the prefix should be stripped from the keys in the resulting sub config
     * @return a new sub config
     */
    public Config subConfig(String namePrefix, boolean stripPrefix) {
        Map<String, String> subConfig = stripPrefix
                ? ConfigUtils.filterAndStripPrefixFromMapKeys(configMap, namePrefix)
                : ConfigUtils.filterMapKeys(configMap, namePrefix);

        return new Config(subConfig);
    }

    /**
     * Creates a new config where based on the provided key filter.
     *
     * @param filter the filter used to decide which keys should be included in the sub config
     * @return a new sub config
     */
    public Config subConfig(Predicate<String> filter) {
        return new Config(ConfigUtils.filterMapKeys(configMap, filter));
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public Config copy() {
        return new Config(new HashMap<>(configMap));
    }

    @Override
    public String toString() {
        return "Config{" +
                "configMapKeys=" + configMap.keySet() +
                '}';
    }

    public String toDetailedString() {
        return "Config{" +
                "configMap=" + configMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(configMap, config.configMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configMap);
    }
    
    private Supplier<MissingValueException> missingValue(String name) {
        return () -> new MissingValueException(name);
    }
}
