package com.zfettostudios.zfuctone.exception;

/**
 * Исключение при обращении к неинициализированному компоненту.
 *
 * @since 0.1
 * @author RandomShel
 */
public class NotInitializedException extends RuntimeException {

    /**
     * Создаёт исключение с заданным сообщением.
     *
     * @param message текст ошибки.
     */
    public NotInitializedException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и причиной ошибки.
     *
     * @param message текст ошибки.
     * @param cause исходная причина ошибки.
     */
    public NotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}