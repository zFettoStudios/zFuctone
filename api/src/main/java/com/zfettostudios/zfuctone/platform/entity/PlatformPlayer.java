package com.zfettostudios.zfuctone.platform.entity;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayer extends Audience {
    <T> T getRawPlayer(@NonNull Class<T> type);
    Object getRawPlayer();

    static @Nullable PlatformPlayer build(@NonNull UUID uuid) {
        return PlatformPlayerImpl.build(uuid);
    }

    static @Nullable PlatformPlayer build(@NonNull Object rawPlayer) {
        return PlatformPlayerImpl.build(rawPlayer);
    }

    @NonNull UUID uuid();
    @NonNull String getNickname();
    @NonNull String getDisplayNickname();
    @NonNull String getLocale();
    @NonNull String getIp();
    int getProtocolVersion();
    @NonNull String getVersion();
    @NonNull String getDisplayVersion();
    @NonNull PlayerEdition getEdition();
    @NonNull String getDisplayEdition();
    boolean isOperator();
    boolean isSneaking();
    boolean isDead();
    boolean isOnline();
    int getPing();
    boolean hasPermission(@NonNull String permission);
    @NonNull PlayerGameMode getGamemode();
    @NonNull String getWorldName();
    @NonNull String getWorldEnvironment();

    PlatformPlayer setDisplayNickname(@NonNull String displayNickname);

    PlatformPlayer dispatchMessage(@NonNull Component message);
    PlatformPlayer executeCommand(@NonNull String command);

    PlatformPlayer kick(@NonNull Component reason);
}
