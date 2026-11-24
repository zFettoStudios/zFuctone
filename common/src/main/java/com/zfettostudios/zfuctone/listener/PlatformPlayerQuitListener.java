package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

import java.util.UUID;

public class PlatformPlayerQuitListener {
    protected final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();
    protected final PlatformPlayerAdapter playerAdapter = ZFuctoneProvider.get().playerAdapter();
    protected final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    protected final ZLogger logger = ZFuctoneProvider.get().logger();

    protected void execute(Object rawPlayer, Runnable removeQuitMessage) {
        UUID uuid = playerAdapter.getUUID(rawPlayer);
        if (uuid == null) {
            logger.warn("uuid player is null");
            return;
        }

        PlatformPlayer player = playerService.getPlatformPlayer(uuid);
        if (player == null) {
            logger.warn("player is null");
            return;
        }

        listenerRegistry.run(PlayerQuitEvent.class, PlayerQuitEvent.build(player, removeQuitMessage));
    }
}
