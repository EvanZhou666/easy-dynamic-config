package com.easyconfig.exception;

/**
 * database query exception
 */
public class QueryConfigException extends RuntimeException{

    public QueryConfigException(String message) {
        super(message);
    }

    public QueryConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
