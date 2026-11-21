package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;

/**
 * Адаптер для взаимодействия с сервером платформы.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PlatformServerAdapter {

    /**
     * Проверяет, существует ли указанный проект на платформе.
     *
     * @param name название проекта.
     * @return {@code true}, если проект существует.
     */
    boolean isProjectExists(@NonNull String name);

    /**
     * Запускает задачу на сервере платформы.
     *
     * @param task выполняемая задача.
     * @return текущий экземпляр адаптера.
     */
    PlatformServerAdapter runTask(@NonNull Runnable task);

    /**
     * Возвращает текущее количество игроков онлайн.
     *
     * @return число игроков онлайн.
     */
    int getPlayersOnline();

    /**
     * Возвращает максимальное количество игроков на сервере.
     *
     * @return лимит слотов сервера.
     */
    int getPlayersMax();

    /**
     * Возвращает значения TPS сервера за интервалы времени.
     *
     * @return массив значений TPS.
     */
    double[] getTPS();

    /**
     * Проверяет, выполняется ли код в основном потоке сервера.
     *
     * @return {@code true}, если текущий поток основной.
     */
    boolean isPrimaryThread();

    /**
     * Регистрирует право на сервере платформы.
     *
     * @param permission регистрируемое право.
     * @param type тип права.
     * @return текущий экземпляр адаптера.
     */
    PlatformServerAdapter registerPermission(@NonNull String permission, @NonNull PermissionType type);

    /**
     * Снимает регистрацию права с сервера платформы.
     *
     * @param permission удаляемое право.
     * @return текущий экземпляр адаптера.
     */
    PlatformServerAdapter unregisterPermission(@NonNull String permission);
}