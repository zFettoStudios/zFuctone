package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.ZFuctone;
import org.jspecify.annotations.NonNull;

/**
 * Поставщик экземпляра {@link ZFuctone}.
 *
 * @since 0.1
 * @author RandomShel
 */
public abstract class ZFuctoneProvider {

    /**
     * Текущий инициализированный экземпляр проекта.
     */
    protected static ZFuctone instance;

    /**
     * Возвращает активный экземпляр {@link ZFuctone}.
     *
     * @return экземпляр проекта или {@code null}, если он ещё не инициализирован.
     */
    public static @NonNull ZFuctone get() {
        return instance;
    }

    /**
     * Проверяет, инициализирован ли экземпляр проекта.
     *
     * @return {@code true}, если экземпляр доступен.
     */
    public static boolean isReady() {
        return instance != null;
    }
}