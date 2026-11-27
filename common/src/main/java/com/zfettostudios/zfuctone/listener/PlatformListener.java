package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

import java.util.UUID;

public class PlatformListener {
    protected final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();
    protected final PlatformPlayerAdapter playerAdapter = ZFuctoneProvider.get().playerAdapter();
    protected final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();

    protected void executePlayerJoin(Object rawPlayer, Runnable removeJoinMessage) {
        PlatformPlayer player = PlatformPlayer.build(rawPlayer);
        if (player == null) return;

        listenerRegistry.run(PlayerJoinEvent.class, PlayerJoinEvent.build(player, removeJoinMessage));
    }

    protected void executePlayerQuit(Object rawPlayer, Runnable removeQuitMessage) {
        UUID uuid = playerAdapter.getUUID(rawPlayer);
        if (uuid == null) return;

        PlatformPlayer player = playerService.getPlatformPlayer(uuid);
        if (player == null) return;

        listenerRegistry.run(PlayerQuitEvent.class, PlayerQuitEvent.build(player, removeQuitMessage));
    }
}