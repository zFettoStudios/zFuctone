package com.zfettostudios.zfuctone.resolver;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;

/**
 * Резолвер рефлексии.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface ReflectionResolver {

    /**
     * Проверяет, доступен ли класс по указанному полному имени.
     *
     * @param className полное имя класса.
     * @return {@code true}, если класс найден и может быть загружен.
     */
    boolean hasClass(@NonNull String className);

    /**
     * Возвращает аннотацию указанного типа с класса, если она присутствует.
     *
     * @param annotationClass класс искомой аннотации.
     * @param target целевой класс.
     * @param <T> тип аннотации.
     * @return найденная аннотация или {@code null}, если аннотация отсутствует.
     */
    <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Class<?> target);

    /**
     * Возвращает аннотацию указанного типа с класса объекта, если она присутствует.
     *
     * @param annotationClass класс искомой аннотации.
     * @param target целевой объект.
     * @param <T> тип аннотации.
     * @return найденная аннотация или {@code null}, если аннотация отсутствует.
     */
    <T extends Annotation> T getAnnotation(@NonNull Class<T> annotationClass, @NonNull Object target);
}