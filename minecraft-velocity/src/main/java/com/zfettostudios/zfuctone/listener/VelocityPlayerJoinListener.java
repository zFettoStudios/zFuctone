package com.zfettostudios.zfuctone.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

public class VelocityPlayerJoinListener extends PlatformPlayerJoinListener {
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        execute(event.getPlayer(), () -> {});
    }
}
