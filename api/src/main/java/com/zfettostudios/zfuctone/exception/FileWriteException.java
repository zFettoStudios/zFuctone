package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public class FileWriteException extends RuntimeException {
    public FileWriteException(@NonNull String file, @NonNull Throwable cause) {
        super("Failed to write " + file + ": " + cause.getMessage(), cause);
    }

    public FileWriteException(@NonNull Path path, @NonNull Throwable cause) {
        super("Failed to write " + path + ": " + cause.getMessage(), cause);
    }
}