package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Реестр плейсхолдеров.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PlaceholderRegistry {

    /**
     * Регистрирует плейсхолдер для модуля с проверкой права и регистрирует его в PlaceholderAPI.
     *
     * @param moduleClass класс модуля.
     * @param name имя плейсхолдера.
     * @param permission требуемое право.
     * @param handler обработчик вычисления плейсхолдера.
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name, @NonNull String permission,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    /**
     * Регистрирует плейсхолдер для модуля без требования права и регистрирует его в PlaceholderAPI.
     *
     * @param moduleClass класс модуля.
     * @param name имя плейсхолдера.
     * @param handler обработчик вычисления плейсхолдера.
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    /**
     * Удаляет плейсхолдер по имени из реестра и PlaceholderAPI.
     *
     * @param name имя плейсхолдера.
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry unregister(@NonNull String name);

    /**
     * Удаляет плейсхолдер указанного модуля по имени из реестра и PlaceholderAPI.
     *
     * @param moduleClass класс модуля.
     * @param name имя плейсхолдера.
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String name);

    /**
     * Удаляет все плейсхолдеры указанного модуля из реестра и PlaceholderAPI.
     *
     * @param moduleClass класс модуля.
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry unregisterAll(@NonNull Class<?> moduleClass);

    /**
     * Удаляет все зарегистрированные плейсхолдеры из реестра и PlaceholderAPI.
     *
     * @return текущий экземпляр реестра.
     */
    PlaceholderRegistry unregisterAll();

    /**
     * Возвращает обработчик плейсхолдера по имени.
     *
     * @param name имя плейсхолдера.
     * @return обработчик плейсхолдера или {@code null}, если не найден.
     */
    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull String name);

    /**
     * Возвращает обработчик плейсхолдера для указанного модуля и имени.
     *
     * @param moduleClass класс модуля.
     * @param name имя плейсхолдера.
     * @return обработчик плейсхолдера или {@code null}, если не найден.
     */
    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull Class<?> moduleClass, @NonNull String name);

    /**
     * Проверяет наличие плейсхолдера с указанным именем в модуле.
     *
     * @param moduleClass класс модуля.
     * @param name имя плейсхолдера.
     * @return {@code true}, если плейсхолдер зарегистрирован в модуле.
     */
    boolean contains(@NonNull Class<?> moduleClass, @NonNull String name);

    /**
     * Проверяет наличие плейсхолдера с указанным именем в реестре.
     *
     * @param name имя плейсхолдера.
     * @return {@code true}, если плейсхолдер зарегистрирован.
     */
    boolean contains(@NonNull String name);

    /**
     * Возвращает карту плейсхолдеров для указанного модуля.
     *
     * @param moduleClass класс модуля.
     * @return карта имён и обработчиков плейсхолдеров модуля или {@code null}, если модуль не найден.
     */
    @Nullable Map<String, Function<@NonNull PlaceholderContext, @Nullable String>> getPlaceholders(@NonNull Class<?> moduleClass);

    /**
     * Возвращает все зарегистрированные плейсхолдеры по модулям.
     *
     * @return карта модулей и их плейсхолдеров.
     */
    @NonNull Map<Class<?>, Map<String, Function<@NonNull PlaceholderContext, @Nullable String>>> getPlaceholders();

    /**
     * Возвращает список созданных плейсхолдеров, зарегистрированных для PlaceholderAPI.
     *
     * @return список плейсхолдеров для PlaceholderAPI.
     */
    @NonNull List<String> getPlaceholderAPIPlaceholders();
}