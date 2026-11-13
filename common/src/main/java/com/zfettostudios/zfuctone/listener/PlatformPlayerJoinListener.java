package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

public abstract class PlatformPlayerJoinListener {
    private PlatformPlayerService playerService() {
        return ZFuctoneAPI.get().playerService();
    }

    protected void execute(Object platformPlayer) {
        playerService().addPlatformPlayer(PlatformPlayer.builder().platformPlayer(platformPlayer).build());
    }
}
