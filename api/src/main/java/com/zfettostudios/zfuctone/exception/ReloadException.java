package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

public class ReloadException extends Exception {
    public ReloadException(@NonNull Throwable cause) {
        super(cause);
    }
}