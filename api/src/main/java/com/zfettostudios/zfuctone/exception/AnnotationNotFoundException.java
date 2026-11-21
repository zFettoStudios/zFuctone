package com.zfettostudios.zfuctone.exception;

import java.lang.annotation.Annotation;

/**
 * Исключение при отсутствии аннотации.
 *
 * @since 0.1
 * @author RandomShel
 */
public class AnnotationNotFoundException extends RuntimeException {

    /**
     * Создаёт исключение для класса, в котором не найдена указанная аннотация.
     *
     * @param target класс, в котором выполнялся поиск.
     * @param annotation искомая аннотация.
     */
    public AnnotationNotFoundException(Class<?> target, Class<? extends Annotation> annotation) {
        super("The annotation \"" + annotation.getName() + "\" was not found in the class \"" + target.getName() + "\"");
    }
}