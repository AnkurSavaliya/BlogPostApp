package com.ankur.BlogPostApp.utils;

public class StringUtils {

    public static boolean isEmpty(String string) {
        if(string == null) {
            return true;
        }
        return string.trim().isEmpty();
    }
}
