package com.zfettostudios.zfuctone.exception;

import java.nio.file.Path;

/**
 * Исключение при ошибке чтения файла.
 *
 * @since 0.1
 * @author RandomShel
 */
public class FileLoadException extends RuntimeException {

    /**
     * Создаёт исключение для файла по строковому пути.
     *
     * @param file путь или имя файла.
     * @param cause исходная причина ошибки.
     */
    public FileLoadException(String file, Throwable cause) {
        super("Failed to read " + file + ": " + cause.getMessage(), cause);
    }

    /**
     * Создаёт исключение для файла по {@link Path}.
     *
     * @param path путь к файлу.
     * @param cause исходная причина ошибки.
     */
    public FileLoadException(Path path, Throwable cause) {
        super("Failed to read " + path + ": " + cause.getMessage(), cause);
    }
}