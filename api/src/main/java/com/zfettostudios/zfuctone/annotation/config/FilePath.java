package com.zfettostudios.zfuctone.annotation.config;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает путь к файлу, используется в основном для конфигурации.
 *
 * @since 0.1
 * @author RandomShel
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilePath {

    /**
     * Путь к файлу относительно папки хранения данных проекта.
     *
     * @return относительный путь к файлу.
     */
    @NonNull String path();

    /**
     * Определяет, задан ли путь относительно папки локализации.
     *
     * @return {@code true} - если путь относителен от папки локализации,
     *         {@code false} - если от основной папки данных проекта.
     */
    boolean localization() default false;
}