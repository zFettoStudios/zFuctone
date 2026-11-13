package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

public abstract class PlatformPlayerJoinListener {
    private PlatformPlayerService playerService() {
        return ZFuctoneAPI.get().playerService();
    }

    private MessageConfig message() {
        return ZFuctoneAPI.get().fileFacade().message();
    }

    private MessageDispatcher messageDispatcher() {
        return ZFuctoneAPI.get().messageDispatcher();
    }

    protected void execute(Object platformPlayer) {
        PlatformPlayer player = PlatformPlayer.builder().platformPlayer(platformPlayer).build();

        playerService().addPlatformPlayer(player);

        if (!message().join().enable()) return;

        messageDispatcher().dispatchEveryone(player, localization -> localization.message().join().format());
    }
}
