package com.zfettostudios.zfuctone.exception;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;

public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(@NonNull Class<?> target, @NonNull Class<? extends Annotation> annotation) {
        super("The annotation \"" + annotation.getName() + "\" was not found in the class \"" + target.getName() + "\"");
    }
}