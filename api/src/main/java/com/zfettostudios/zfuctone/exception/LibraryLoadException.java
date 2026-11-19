package com.zfettostudios.zfuctone.exception;

public class LibraryLoadException extends RuntimeException {
    public LibraryLoadException(String message) {
        super(message);
    }

    public LibraryLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
