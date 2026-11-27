package com.zfettostudios.zfuctone.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

public class VelocityListener extends PlatformListener {
    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        executePlayerJoin(event.getPlayer(), () -> {});
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        executePlayerQuit(event.getPlayer(), () -> {});
    }
}
