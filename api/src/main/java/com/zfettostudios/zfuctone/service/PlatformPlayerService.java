package com.zfettostudios.zfuctone.service;

import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис хранения и управление игроками.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PlatformPlayerService extends Reloadable<PlatformPlayerService> {

    /**
     * Возвращает игрока платформы по его UUID.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если не найден.
     */
    @Nullable PlatformPlayer getPlatformPlayer(@NonNull UUID uuid);

    /**
     * Возвращает игрока платформы по его нику.
     *
     * @param nickname ник игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если не найден.
     */
    @Nullable PlatformPlayer getPlatformPlayer(@NonNull String nickname);

    /**
     * Возвращает список всех сохранённых игроков платформы.
     *
     * @return список игроков платформы.
     */
    @NonNull List<PlatformPlayer> getPlatformPlayers();

    /**
     * Добавляет игрока платформы в сервис.
     *
     * @param platformPlayer добавляемый игрок платформы.
     * @return текущий экземпляр сервиса.
     */
    PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer);

    /**
     * Удаляет игрока платформы из сервиса по его UUID.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return текущий экземпляр сервиса.
     */
    PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid);

    /**
     * Удаляет игрока платформы из сервиса по его нику.
     *
     * @param nickname ник игрока.
     * @return текущий экземпляр сервиса.
     */
    PlatformPlayerService removePlatformPlayer(@NonNull String nickname);

    /**
     * Возвращает список игроков платформы, находящихся в сети.
     *
     * @return список игроков онлайн.
     */
    @NonNull List<PlatformPlayer> getOnlinePlayers();
}