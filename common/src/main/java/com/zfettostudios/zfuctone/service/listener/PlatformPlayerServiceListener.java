package com.zfettostudios.zfuctone.service.listener;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;

@RequiredArgsConstructor
public class PlatformPlayerServiceListener implements ZFListener {
    private final PlatformPlayerService playerService;

    @EventHandler(priority = Event.Priority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerService.addPlatformPlayer(event.player());
    }

    @EventHandler(priority = Event.Priority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerService.removePlatformPlayer(event.player().uuid());
    }
}
