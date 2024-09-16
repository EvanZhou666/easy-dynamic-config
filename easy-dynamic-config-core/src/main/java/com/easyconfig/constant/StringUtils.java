package com.easyconfig.constant;

public class StringUtils {

    public static final String EMPTY = "";

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;

    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);

    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


}
