package com.zfettostudios.zfuctone.platform.adapter;

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

    @Nullable UUID getUniqueIdFromNickname(@NonNull String nickname);

    @NonNull String getVersionFromProtocolVersion(int protocol);
    @NonNull String getVersionFromProtocolVersion(@NonNull String formatOne, @NonNull String format, int protocol);

    @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull UUID uuid, @NonNull Component message);
    @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull Object platformPlayer, @NonNull Component message);

    @NonNull PlatformPlayerAdapter executeCommand(@NonNull UUID uuid, @NonNull String command);
    @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command);

    @NonNull String getNickname(@NonNull UUID uuid);
    @NonNull String getNickname(@NonNull Object platformPlayer);

    @Nullable UUID getUniqueId(@NonNull Object platformPlayer);

    int getPing(@NonNull UUID uuid);
    int getPing(@NonNull Object platformPlayer);

    @NonNull String getLocale(@NonNull UUID uuid);
    @NonNull String getLocale(@NonNull Object platformPlayer);

    @Nullable String getIp(@NonNull UUID uuid);
    @Nullable String getIp(@NonNull Object platformPlayer);

    boolean isOperator(@NonNull UUID uuid);
    boolean isOperator(@NonNull Object platformPlayer);

    boolean isSneaking(@NonNull UUID uuid);
    boolean isSneaking(@NonNull Object platformPlayer);

    boolean isDead(@NonNull UUID uuid);
    boolean isDead(@NonNull Object platformPlayer);

    boolean isOnline(@NonNull UUID uuid);
    boolean isOnline(@NonNull Object platformPlayer);

    PlatformPlayerAdapter kick(@NonNull UUID uuid, @NonNull Component reason);
    PlatformPlayerAdapter kick(@NonNull Object platformPlayer, @NonNull Component reason);
}
