package com.mark.moveandswipervitem.exception;

/**
 * Created by chenzhen on 2018/8/17.
 */

public class NotInitializeException extends ExceptionInInitializerError {

    public NotInitializeException() {
    }

    public NotInitializeException(String message) {
        super(message);
    }
}
