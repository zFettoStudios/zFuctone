package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.Gamemode;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayerAdapter {
    @Nullable <T> T getPlatformPlayer(@NonNull UUID uuid, @NonNull Class<T> target);
    @Nullable Object getPlatformPlayer(@NonNull UUID uuid);

    boolean isPlatformPlayer(Object platformPlayer);

    int getProtocolVersion(@NonNull UUID uuid);
    int getProtocolVersion(@NonNull Object platformPlayer);

    @Nullable UUID getUUIDFromNickname(@NonNull String nickname);

    @NonNull String getVersionFromProtocolVersion(int protocol);
    @NonNull String getVersionFromProtocolVersion(@NonNull String formatOne, @NonNull String format, int protocol);

    @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull Object platformPlayer, @NonNull Component message);

    @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command);

    @NonNull String getNickname(@NonNull Object platformPlayer);

    @Nullable UUID getUUID(@NonNull Object platformPlayer);

    int getPing(@NonNull Object platformPlayer);

    @NonNull String getLocale(@NonNull UUID uuid);
    @NonNull String getLocale(@NonNull Object platformPlayer);

    @Nullable String getIp(@NonNull Object platformPlayer);

    boolean isOperator(@NonNull Object platformPlayer);

    boolean isSneaking(@NonNull Object platformPlayer);

    boolean isDead(@NonNull Object platformPlayer);

    boolean isOnline(@NonNull Object platformPlayer);

    boolean hasPermission(@NonNull String permission, @NonNull Object platformPlayer);

    PlatformPlayerAdapter kick(@NonNull Object platformPlayer, @NonNull Component reason);

    @NonNull Gamemode getGamemode(@NonNull Object platformPlayer);

    @NonNull String getWorldName(@NonNull Object platformPlayer);

    @NonNull String getWorldEnvironment(@NonNull Object platformPlayer);
}
