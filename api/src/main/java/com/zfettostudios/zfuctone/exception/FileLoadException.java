package com.zfettostudios.zfuctone.exception;

import java.nio.file.Path;

public class FileLoadException extends RuntimeException {
    public FileLoadException(String file, Throwable cause) {
        super("Failed to read " + file + "\n" + cause.getMessage());
    }

    public FileLoadException(Path path, Throwable cause) {
        super("Failed to read " + path + "\n" + cause.getMessage());
    }
}
