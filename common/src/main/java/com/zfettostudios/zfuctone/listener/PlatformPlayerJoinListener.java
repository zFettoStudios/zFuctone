package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.provider.FactoryProvider;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.factory.PlatformPlayerFactory;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

public abstract class PlatformPlayerJoinListener {
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    private final MessageDispatcher messageDispatcher = ZFuctoneProvider.get().messageDispatcher();
    private final MessageConfig config = ZFuctoneProvider.get().fileFacade().message();
    private final PlatformPlayerFactory playerFactory = FactoryProvider.playerFactory();

    protected void execute(Object platformPlayer) {
        PlatformPlayer player = playerFactory.build(platformPlayer);
        if (player == null) return;

        playerService.addPlatformPlayer(player);

        if (!config.join().enable()) return;

        messageDispatcher.dispatchEveryone(player, localization -> localization.message().join().format().asString());
    }
}
