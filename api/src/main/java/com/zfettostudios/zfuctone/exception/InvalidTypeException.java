package com.zfettostudios.zfuctone.exception;

import org.slf4j.helpers.MessageFormatter;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException(Class<?> target) {
        super("Invalid type \"" + target.getName() + "\"");
    }
    public InvalidTypeException(String message) {
        super(message);
    }
    public InvalidTypeException(String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
}
