package com.zfettostudios.zfuctone.exception;

import java.nio.file.Path;

/**
 * Исключение при ошибке записи в файл.
 *
 * @since 0.1
 * @author RandomShel
 */
public class FileWriteException extends RuntimeException {

    /**
     * Создаёт исключение для файла по строковому пути.
     *
     * @param file путь или имя файла.
     * @param cause исходная причина ошибки.
     */
    public FileWriteException(String file, Throwable cause) {
        super("Failed to write " + file + ": " + cause.getMessage(), cause);
    }

    /**
     * Создаёт исключение для файла по {@link Path}.
     *
     * @param path путь к файлу.
     * @param cause исходная причина ошибки.
     */
    public FileWriteException(Path path, Throwable cause) {
        super("Failed to write " + path + ": " + cause.getMessage(), cause);
    }
}