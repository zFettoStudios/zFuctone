package com.zfettostudios.zfuctone.service;

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
    private final Map<UUID, PlatformPlayer> platformPlayers = new ConcurrentHashMap<>();
    private final PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get().playerAdapter();

    @Override
    public PlatformPlayerService enable() {
        return this;
    }

    @Override
    public PlatformPlayerService disable() {
        return this;
    }

    @Override
    public @Nullable PlatformPlayer getPlatformPlayer(@NonNull UUID uuid) {
        return platformPlayers.get(uuid);
    }

    @Override
    public @Nullable PlatformPlayer getPlatformPlayer(@NonNull String nickname) {
        return platformPlayers.get(playerAdapter.getUUIDFromNickname(nickname));
    }

    @Override
    public @NonNull List<PlatformPlayer> getPlatformPlayers() {
        return new LinkedList<>(platformPlayers.values());
    }

    @Override
    public PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer) {
        platformPlayers.put(platformPlayer.uuid(), platformPlayer);
        return this;
    }

    @Override
    public PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid) {
        platformPlayers.remove(uuid);
        return this;
    }

    @Override
    public PlatformPlayerService removePlatformPlayer(@NonNull String nickname) {
        platformPlayers.remove(playerAdapter.getUUIDFromNickname(nickname));
        return this;
    }

    @Override
    public @NonNull List<PlatformPlayer> getOnlinePlayers() {
        return List.copyOf(platformPlayers.values());
    }

    @Override
    public PlatformPlayerService reload() {
        return this;
    }
}
