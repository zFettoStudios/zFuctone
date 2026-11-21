package com.zfettostudios.zfuctone.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * Исключение при неподдерживаемом или некорректном типе.
 *
 * @since 0.1
 * @author RandomShel
 */
public class InvalidTypeException extends RuntimeException {

    /**
     * Создаёт исключение для некорректного класса.
     *
     * @param target неподдерживаемый класс.
     */
    public InvalidTypeException(Class<?> target) {
        super("Invalid type \"" + target.getName() + "\"");
    }

    /**
     * Создаёт исключение с готовым сообщением.
     *
     * @param message текст ошибки.
     */
    public InvalidTypeException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с форматированием в стиле SLF4J.
     *
     * @param format шаблон сообщения с плейсхолдерами {@code {}}.
     * @param arguments аргументы для подстановки.
     */
    public InvalidTypeException(String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage(),
            MessageFormatter.arrayFormat(format, arguments).getThrowable());
    }
}