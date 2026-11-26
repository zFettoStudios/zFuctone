package com.zfettostudios.zfuctone.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class VelocityPlayerQuitListener extends PlatformPlayerQuitListener {
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        execute(event.getPlayer(), () -> {});
    }
}
