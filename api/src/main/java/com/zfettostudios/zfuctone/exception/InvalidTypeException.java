package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;
import org.slf4j.helpers.MessageFormatter;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException(@NonNull Class<?> target) {
        super("Invalid type \"" + target.getName() + "\"");
    }

    public InvalidTypeException(@NonNull String message) {
        super(message);
    }

    public InvalidTypeException(@NonNull String format, @NonNull Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage(),
            MessageFormatter.arrayFormat(format, arguments).getThrowable());
    }
}