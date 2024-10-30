package com.github.alexgaard.scribe.util;

import com.github.alexgaard.scribe.exception.InvalidValueException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExceptionUtils {

    private ExceptionUtils() {}


    /**
     * Uses template type erasure to trick the compiler into removing checking of exception. The compiler
     * treats E as RuntimeException, meaning that softenException doesn't need to declare it,
     * but the runtime treats E as Exception (because of type erasure), which avoids {@link ClassCastException}.
     * @param t throwable to soften
     * @return the throwable provided
     * @param <T> type of throwable
     * @throws T the same throwable that was provided
     */
    public static <T extends Throwable> T soften(Throwable t) throws T {
        if (t == null) throw new RuntimeException();
        //noinspection unchecked
        throw (T) t;
    }

    public static <R> R soften(UnsafeSupplier<R> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw soften(e);
        }
    }

    public static <T, R> Function<T, R> soften(UnsafeFunction<T, R> supplier) {
        return (T val) -> {
            try {
                return supplier.apply(val);
            } catch (Exception e) {
                throw soften(e);
            }
        };
    }

    public static <R> Function<String, R> wrapValueParsingException(String key, UnsafeFunction<String, R> func) {
        return (String value) -> {
            try {
                return func.apply(value);
            } catch (Exception e) {
                throw new InvalidValueException(key, value, e);
            }
        };
    }

    public static <R> Function<List<String>, List<R>> wrapValuesParsingException(String key, UnsafeFunction<String, R> func) {
        return (List<String> values) -> {
            try {
                return values.stream()
                        .map(soften(func))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                throw new InvalidValueException(key, values, e);
            }
        };
    }

    public interface UnsafeFunction<T, R> {
        R apply(T t) throws Exception;
    }

    public interface UnsafeSupplier<R> {
        R get() throws Exception;
    }

}
