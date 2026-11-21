package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Реестр прав.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PermissionRegistry {

    /**
     * Регистрирует право для указанного модуля с заданным типом.
     *
     * @param moduleClass класс модуля.
     * @param permission регистрируемое право.
     * @param type тип права.
     * @return текущий экземпляр реестра.
     */
    PermissionRegistry register(@NonNull Class<?> moduleClass, @NonNull String permission, @NonNull PermissionType type);

    /**
     * Удаляет право из реестра.
     *
     * @param permission удаляемое право.
     * @return текущий экземпляр реестра.
     */
    PermissionRegistry unregister(@NonNull String permission);

    /**
     * Удаляет право указанного модуля из реестра.
     *
     * @param moduleClass класс модуля.
     * @param permission удаляемое право.
     * @return текущий экземпляр реестра.
     */
    PermissionRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String permission);

    /**
     * Удаляет все права указанного модуля из реестра.
     *
     * @param moduleClass класс модуля.
     * @return текущий экземпляр реестра.
     */
    PermissionRegistry unregisterAll(@NonNull Class<?> moduleClass);

    /**
     * Удаляет все зарегистрированные права из реестра.
     *
     * @return текущий экземпляр реестра.
     */
    PermissionRegistry unregisterAll();

    /**
     * Возвращает набор прав для указанного модуля.
     *
     * @param moduleClass класс модуля.
     * @return набор прав модуля или {@code null}, если модуль не найден.
     */
    @Nullable Set<String> getPermissions(@NonNull Class<?> moduleClass);

    /**
     * Возвращает все зарегистрированные права по модулям.
     *
     * @return карта модулей и наборов их прав.
     */
    @NonNull Map<Class<?>, Set<String>> getPermissions();

    /**
     * Проверяет наличие права в указанном модуле.
     *
     * @param moduleClass класс модуля.
     * @param permission проверяемое право.
     * @return {@code true}, если право зарегистрировано в модуле.
     */
    boolean contains(@NonNull Class<?> moduleClass, @NonNull String permission);

    /**
     * Проверяет наличие права в реестре.
     *
     * @param permission проверяемое право.
     * @return {@code true}, если право зарегистрировано.
     */
    boolean contains(@NonNull String permission);
}