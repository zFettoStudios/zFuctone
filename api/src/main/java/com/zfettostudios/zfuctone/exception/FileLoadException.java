package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public class FileLoadException extends RuntimeException {
    public FileLoadException(@NonNull String file, @NonNull Throwable cause) {
        super("Failed to read " + file + ": " + cause.getMessage(), cause);
    }

    public FileLoadException(@NonNull Path path, @NonNull Throwable cause) {
        super("Failed to read " + path + ": " + cause.getMessage(), cause);
    }
}