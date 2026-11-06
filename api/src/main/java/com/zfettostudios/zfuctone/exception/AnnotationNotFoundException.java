package com.zfettostudios.zfuctone.exception;

public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(Class<?> target, Class<?> annotation) {
        super("The annotation \"" + annotation.getName() + "\" was not found in the class \"" + target.getName() + "\"");
    }
}
