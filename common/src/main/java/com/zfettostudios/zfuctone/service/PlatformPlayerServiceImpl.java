package com.zfettostudios.zfuctone.service;

import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformPlayerServiceImpl implements PlatformPlayerService {
    Map<UUID, PlatformPlayer> platformPlayers = new ConcurrentHashMap<>();
    @NonFinal PlatformPlayerAdapter playerAdapter;

    @Override
    public @Nullable PlatformPlayer getPlatformPlayer(@NonNull UUID uuid) {
        isNullGet();
        return platformPlayers.get(uuid);
    }

    @Override
    public @Nullable PlatformPlayer getPlatformPlayer(@NonNull String nickname) {
        isNullGet();
        return platformPlayers.get(playerAdapter.getUniqueIdFromNickname(nickname));
    }

    @Override
    public @NonNull List<PlatformPlayer> getPlatformPlayers() {
        return new LinkedList<>(platformPlayers.values());
    }

    @Override
    public PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer) {
        platformPlayers.put(platformPlayer.getUniqueId(), platformPlayer);
        return this;
    }

    @Override
    public PlatformPlayerService updateAll() {
        for (final PlatformPlayer platformPlayer : platformPlayers.values()) {

        }
        return this;
    }

    @Override
    public PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid) {
        platformPlayers.remove(uuid);
        return this;
    }

    @Override
    public PlatformPlayerService removePlatformPlayer(@NonNull String nickname) {
        platformPlayers.remove(playerAdapter.getUniqueIdFromNickname(nickname));
        return this;
    }

    @Override
    public PlatformPlayerService update(@NonNull UUID uuid) {
        return this;
    }

    @Override
    public PlatformPlayerService update(@NonNull String nickname) {
        return this;
    }

    private void isNullGet() {
        if (playerAdapter == null) playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);
    }
}
