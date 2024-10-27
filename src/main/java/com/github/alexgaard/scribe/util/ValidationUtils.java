package com.github.alexgaard.scribe.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static boolean isEmailValid(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidPortNumber(int portNumber) {
        return portNumber < 0 || portNumber > 65535;
    }

}
