package com.zfettostudios.zfuctone.platform.entity;

import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface PlatformPlayer {
    <T> T getRawPlayer(@NonNull Class<T> type);
    Object getRawPlayer();

    static PlatformPlayerImpl.Builder builder() {
        return PlatformPlayerImpl.builder();
    }

    boolean isPlayer();

    @NonNull UUID getUniqueId();
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

    PlatformPlayer setDisplayNickname(String displayNickname);

    PlatformPlayer dispatchMessage(@NonNull Component message);
    PlatformPlayer executeCommand(@NonNull String command);
}
