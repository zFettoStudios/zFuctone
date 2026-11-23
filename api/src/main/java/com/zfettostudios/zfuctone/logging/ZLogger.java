package com.zfettostudios.zfuctone.logging;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

/**
 * Логгер.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface ZLogger extends PlatformPlayer {

    /**
     * Логирует начало включения.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logEnabling();

    /**
     * Логирует успешное включение.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logEnabled();

    /**
     * Логирует начало отключения.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logDisabling();

    /**
     * Логирует успешное отключение.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logDisabled();

    /**
     * Логирует начало перезагрузки.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logReloading();

    /**
     * Логирует успешную перезагрузку.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logReloaded();

    /**
     * Логирует описание.
     *
     * @return текущий экземпляр логгера.
     */
    ZLogger logDescription();

    /**
     * Логирует предупреждение с форматированием через плейсхолдеры {@code {}}.
     *
     * @param format шаблон сообщения с плейсхолдерами {@code {}}.
     * @param arguments аргументы для подстановки.
     * @return текущий экземпляр логгера.
     */
    ZLogger warn(@NonNull String format, @NonNull Object... arguments);

    /**
     * Логирует предупреждение простым текстом.
     *
     * @param message текст предупреждения.
     * @return текущий экземпляр логгера.
     */
    ZLogger warn(@NonNull String message);

    /**
     * Логирует предупреждение в виде компонента Adventure.
     *
     * @param component компонент сообщения.
     * @return текущий экземпляр логгера.
     */
    ZLogger warn(@NonNull Component component);

    /**
     * Логирует информационное сообщение с форматированием через плейсхолдеры {@code {}}.
     *
     * @param format шаблон сообщения с плейсхолдерами {@code {}}.
     * @param arguments аргументы для подстановки.
     * @return текущий экземпляр логгера.
     */
    ZLogger info(@NonNull String format, @NonNull Object... arguments);

    /**
     * Логирует информационное сообщение простым текстом.
     *
     * @param message текст сообщения.
     * @return текущий экземпляр логгера.
     */
    ZLogger info(@NonNull String message);

    /**
     * Логирует информационное сообщение в виде компонента Adventure.
     *
     * @param message компонент сообщения.
     * @return текущий экземпляр логгера.
     */
    ZLogger info(@NonNull Component message);
}