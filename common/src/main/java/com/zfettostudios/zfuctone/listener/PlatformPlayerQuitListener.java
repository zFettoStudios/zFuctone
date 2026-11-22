package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

import java.util.UUID;

public abstract class PlatformPlayerQuitListener {
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    private final MessageDispatcher messageDispatcher = ZFuctoneProvider.get().messageDispatcher();
    private final MessageConfig config = ZFuctoneProvider.get().fileFacade().message();
    private final PlatformPlayerAdapter playerAdapter = ZFuctoneProvider.get().playerAdapter();

    protected void execute(Object platformPlayer) {
        UUID uuid = playerAdapter.getUUID(platformPlayer);
        if (uuid == null) return;

        PlatformPlayer player = playerService.getPlatformPlayer(uuid);
        if (player == null) return;

        if (!config.quit().enable()) return;

        messageDispatcher.dispatchEveryone(player, localization -> localization.message().quit().format().asString());

        playerService.removePlatformPlayer(uuid);
    }
}