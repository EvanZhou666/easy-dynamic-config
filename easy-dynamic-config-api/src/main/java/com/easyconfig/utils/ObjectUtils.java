package com.easyconfig.utils;

public class ObjectUtils {

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj must not be null");
        }
    }

    public static void checkNotNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

}
