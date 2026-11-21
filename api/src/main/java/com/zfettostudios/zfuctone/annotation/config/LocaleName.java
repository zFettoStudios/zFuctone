package com.zfettostudios.zfuctone.annotation.config;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает код языка в формате {@code ru_ru}.
 *
 * @since 0.1
 * @author RandomShel
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocaleName {
    @NonNull String value();
}