package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayerAdapter {
    @Nullable <T> T getPlatformPlayer(@NonNull UUID uuid, @NonNull Class<T> target);
    @Nullable Object getPlatformPlayer(@NonNull UUID uuid);

    boolean isPlatformPlayer(Object platformPlayer);

    @Nullable UUID getUUIDFromNickname(@NonNull String nickname);

    @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command);

    @NonNull String getNickname(@NonNull Object platformPlayer);

    @Nullable UUID getUUID(@NonNull Object platformPlayer);

    int getPing(@NonNull Object platformPlayer);

    @NonNull String getLocale(@NonNull Object platformPlayer);

    boolean isOperator(@NonNull Object platformPlayer);

    boolean isSneaking(@NonNull Object platformPlayer);

    boolean isDead(@NonNull Object platformPlayer);

    boolean isOnline(@NonNull Object platformPlayer);

    boolean hasPermission(@NonNull Object platformPlayer, @NonNull String permission);

    @NonNull PlayerGameMode getGamemode(@NonNull Object platformPlayer);

    @NonNull String getWorldName(@NonNull Object platformPlayer);

    @NonNull String getWorldEnvironment(@NonNull Object platformPlayer);
}
