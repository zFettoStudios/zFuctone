package com.zfettostudios.zfuctone.reflect;

import com.zfettostudios.zfuctone.exception.AnnotationNotFoundException;
import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;

public class ReflectionResolverImpl implements ReflectionResolver {
    @Override
    public boolean hasClass(@NonNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException _) {
            return false;
        }
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Class<?> target) {
        T annotation = target.getAnnotation(annotationClass);
        if (annotation == null) throw new AnnotationNotFoundException(target, annotationClass);

        return annotation;
    }

    @Override
    public <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Object target) {
        T annotation = target.getClass().getAnnotation(annotationClass);
        if (annotation == null) throw new AnnotationNotFoundException(target.getClass(), annotationClass);

        return annotation;
    }
}
