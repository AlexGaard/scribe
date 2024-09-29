package com.github.alexgaard.config.exception;

import java.util.function.Function;

public class ExceptionUtil {

    private ExceptionUtil() {}


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

    public static <R> Function<String, R> wrapException(String key, UnsafeFunction<String, R> func) {
        return (String value) -> {
            try {
                return func.apply(value);
            } catch (Exception e) {
                throw new InvalidValueException(key, value, e);
            }
        };
    }

    public interface UnsafeFunction<T, R> {
        R apply(T t) throws Exception;
    }

}
