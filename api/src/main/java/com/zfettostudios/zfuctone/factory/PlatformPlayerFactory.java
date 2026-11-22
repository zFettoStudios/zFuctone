package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayerFactory {
    /**
     * Создаёт экземпляр игрока по UUID.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если игрок не найден.
     */
    @Nullable PlatformPlayer build(@NonNull UUID uuid);

    /**
     * Создаёт экземпляр игрока по объекту платформенного игрока.
     *
     * @param rawPlayer объект игрока платформы.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если объект не является игроком платформы.
     */
    @Nullable PlatformPlayer build(@NonNull Object rawPlayer);
}
