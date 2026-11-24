package com.zfettostudios.zfuctone.service;

import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface PlatformPlayerService extends Reloadable<PlatformPlayerService> {
    @Nullable PlatformPlayer getPlatformPlayer(@NonNull UUID uuid);

    @Nullable PlatformPlayer getPlatformPlayer(@NonNull String nickname);

    @NonNull List<PlatformPlayer> getPlatformPlayers();

    @NonNull PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer);

    @NonNull PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid);

    @NonNull PlatformPlayerService removePlatformPlayer(@NonNull String nickname);

    @NonNull List<PlatformPlayer> getOnlinePlayers();
}