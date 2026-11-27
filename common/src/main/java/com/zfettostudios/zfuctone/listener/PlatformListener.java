package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.util.LazyInstance;

import java.util.UUID;

public class PlatformListener {
    protected final LazyInstance<ListenerRegistry> listenerRegistry = LazyInstance.build(ZFuctone::listenerRegistry);
    protected final LazyInstance<PlatformPlayerAdapter> playerAdapter = LazyInstance.build(ZFuctone::playerAdapter);
    protected final LazyInstance<PlatformPlayerService> playerService = LazyInstance.build(ZFuctone::playerService);

    protected void executePlayerJoin(Object rawPlayer, Runnable removeJoinMessage) {
        PlatformPlayer player = PlatformPlayer.build(rawPlayer);
        if (player == null) return;

        listenerRegistry.get().run(PlayerJoinEvent.class, PlayerJoinEvent.build(player, removeJoinMessage));
    }

    protected void executePlayerQuit(Object rawPlayer, Runnable removeQuitMessage) {
        UUID uuid = playerAdapter.get().getUUID(rawPlayer);
        if (uuid == null) return;

        PlatformPlayer player = playerService.get().getPlatformPlayer(uuid);
        if (player == null) return;

        listenerRegistry.get().run(PlayerQuitEvent.class, PlayerQuitEvent.build(player, removeQuitMessage));
    }
}