package com.zfettostudios.zfuctone.exception;

/**
 * Исключение при ошибке перезагрузки.
 *
 * @since 0.1
 * @author RandomShel
 */
public class ReloadException extends Exception {

    /**
     * Создаёт исключение с указанием причины сбоя при перезагрузке.
     *
     * @param cause исходная причина ошибки.
     */
    public ReloadException(Throwable cause) {
        super(cause);
    }
}