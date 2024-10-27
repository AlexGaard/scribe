package com.github.alexgaard.scribe.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private final static String emailPatternStr = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    private final static Pattern emailPattern = Pattern.compile(emailPatternStr);

    public static boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

}
