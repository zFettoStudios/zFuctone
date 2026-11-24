package com.zfettostudios.zfuctone.service;

import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.listener.PlatformPlayerServiceListener;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformPlayerServiceImpl implements PlatformPlayerService {
    private final Map<UUID, PlatformPlayer> platformPlayers = new ConcurrentHashMap<>();
    private final PlatformPlayerAdapter playerAdapter = ZFuctoneProvider.get().playerAdapter();
    private final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();

    private final PlatformPlayerServiceListener listener = new PlatformPlayerServiceListener(this);

    @Override
    public @NonNull PlatformPlayerService enable() {
        listenerRegistry.register(PlatformPlayerService.class, listener);
        return this;
    }

    @Override
    public @NonNull PlatformPlayerService disable() {
        listenerRegistry.unregisterAll(PlatformPlayerService.class);
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
    public @NonNull PlatformPlayerService addPlatformPlayer(@NonNull PlatformPlayer platformPlayer) {
        platformPlayers.put(platformPlayer.uuid(), platformPlayer);
        return this;
    }

    @Override
    public @NonNull PlatformPlayerService removePlatformPlayer(@NonNull UUID uuid) {
        platformPlayers.remove(uuid);
        return this;
    }

    @Override
    public @NonNull PlatformPlayerService removePlatformPlayer(@NonNull String nickname) {
        platformPlayers.remove(playerAdapter.getUUIDFromNickname(nickname));
        return this;
    }

    @Override
    public @NonNull List<PlatformPlayer> getOnlinePlayers() {
        return List.copyOf(platformPlayers.values());
    }

    @Override
    public @NonNull PlatformPlayerService reload() {
        return this;
    }
}
