package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

public class LibraryLoadException extends RuntimeException {

    public LibraryLoadException(@NonNull String message) {
        super(message);
    }

    public LibraryLoadException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}