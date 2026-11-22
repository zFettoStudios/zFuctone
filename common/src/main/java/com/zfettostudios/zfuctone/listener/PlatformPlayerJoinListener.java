package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;

public class PlatformPlayerJoinListener {
    protected final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();
    protected final ZLogger logger = ZFuctoneProvider.get().logger();

    protected void execute(Object rawPlayer) {
        PlatformPlayer player = PlatformPlayer.build(rawPlayer);
        if (player == null) {
            logger.warn("player is null");
            return;
        }

        listenerRegistry.run(PlayerJoinEvent.class, PlayerJoinEvent.build(player));
    }
}
