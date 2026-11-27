package com.zfettostudios.zfuctone.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener extends PlatformListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        executePlayerJoin(event.getPlayer(), () -> event.setJoinMessage(null));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        executePlayerQuit(event.getPlayer(), () -> event.setQuitMessage(null));
    }
}
