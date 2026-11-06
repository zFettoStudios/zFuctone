package com.zfettostudios.zfuctone.exception;

import java.nio.file.Path;

public class FileWriteException extends RuntimeException {
    public FileWriteException(String file, Throwable cause) {
        super("Failed to write " + file + "\n" + cause.getMessage());
    }

    public FileWriteException(Path path, Throwable cause) {
        super("Failed to write " + path + "\n" + cause.getMessage());
    }
}
