package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

public abstract class PlatformPlayerJoinListener {
    private final PlatformPlayerService playerService = ZFuctoneAPI.get().playerService();
    private final MessageDispatcher messageDispatcher = ZFuctoneAPI.get().messageDispatcher();
    private final MessageConfig config = ZFuctoneAPI.get().fileFacade().message();

    protected void execute(Object platformPlayer) {
        PlatformPlayer player = PlatformPlayer.build(platformPlayer);
        if (player == null) return;

        playerService.addPlatformPlayer(player);

        if (!config.join().enable()) return;

        messageDispatcher.dispatchEveryone(player, localization -> localization.message().join().format().asString());
    }
}
