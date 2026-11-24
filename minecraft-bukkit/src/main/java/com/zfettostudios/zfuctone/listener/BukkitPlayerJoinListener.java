package com.zfettostudios.zfuctone.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitPlayerJoinListener extends PlatformPlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        execute(event.getPlayer(), () -> event.setJoinMessage(null));
    }
}
