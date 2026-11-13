package com.zfettostudios.zfuctone.listener;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

import java.util.UUID;

public abstract class PlatformPlayerQuitListener {
    private PlatformPlayerService playerService() {
        return ZFuctoneAPI.get().playerService();
    }

    private PlatformPlayerAdapter playerAdapter() {
        return ZFuctoneAPI.get().playerAdapter();
    }

    protected void execute(Object platformPlayer) {
        UUID uuid = playerAdapter().getUniqueId(platformPlayer);
        if (uuid != null) playerService().removePlatformPlayer(uuid);
    }
}