package com.zfettostudios.zfuctone.resolver;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;

public interface ReflectionResolver {
    boolean hasClass(@NonNull String className);

    @NonNull <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Class<?> target);

    @NonNull <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Object target);
}