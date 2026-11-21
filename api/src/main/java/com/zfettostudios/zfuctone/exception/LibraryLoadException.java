package com.zfettostudios.zfuctone.exception;

/**
 * Исключение при ошибке загрузки библиотеки.
 *
 * @since 0.1
 * @author RandomShel
 */
public class LibraryLoadException extends RuntimeException {

    /**
     * Создаёт исключение с заданным сообщением.
     *
     * @param message текст ошибки.
     */
    public LibraryLoadException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и причиной ошибки.
     *
     * @param message текст ошибки.
     * @param cause исходная причина ошибки.
     */
    public LibraryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}