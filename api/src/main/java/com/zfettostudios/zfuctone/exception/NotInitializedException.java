package com.zfettostudios.zfuctone.exception;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException(String message) {
        super(message);
    }

    public NotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
