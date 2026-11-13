package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

import java.util.UUID;

public abstract class PlatformPlayerQuitListener {
    private PlatformPlayerService playerService() {
        return ZFuctoneAPI.get().playerService();
    }

    private PlatformPlayerAdapter playerAdapter() {
        return ZFuctoneAPI.get().playerAdapter();
    }

    private MessageConfig message() {
        return ZFuctoneAPI.get().fileFacade().message();
    }

    private MessageDispatcher messageDispatcher() {
        return ZFuctoneAPI.get().messageDispatcher();
    }

    protected void execute(Object platformPlayer) {
        UUID uuid = playerAdapter().getUniqueId(platformPlayer);
        if (uuid == null) return;

        PlatformPlayer player = playerService().getPlatformPlayer(uuid);
        if (player == null) return;

        if (!message().quit().enable()) return;

        messageDispatcher().dispatchEveryone(player, localization -> localization.message().quit().format());

        playerService().removePlatformPlayer(uuid);
    }
}