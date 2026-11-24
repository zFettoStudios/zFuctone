package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException(@NonNull String message) {
        super(message);
    }

    public NotInitializedException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}