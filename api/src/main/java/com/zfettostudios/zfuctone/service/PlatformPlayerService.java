package com.zfettostudios.zfuctone.service;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface PlatformPlayerService extends Module<PlatformPlayerService> {
    @Nullable PlatformPlayer getPlatformPlayer(@NonNull UUID uuid);
    @Nullable PlatformPlayer getPlatformPlayer(@NonNull String nickname);
    @NonNull List<PlatformPlayer> getPlatformPlayers();
    PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer);
    PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid);
    PlatformPlayerService removePlatformPlayer(@NonNull String nickname);
}
