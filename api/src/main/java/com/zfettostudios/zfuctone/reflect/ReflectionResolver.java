package com.zfettostudios.zfuctone.reflect;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;

public interface ReflectionResolver {
    boolean hasClass(@NonNull String className);
    <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Class<?> target);
    <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Object target);
}
