package com.zfettostudios.zfuctone.model.event.player;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;

@Getter
@Setter
@Accessors(fluent = true)
public class PlayerQuitEventImpl implements PlayerQuitEvent {
    private PlatformPlayer player;
    private boolean cancelled = false;

    public PlayerQuitEventImpl(PlatformPlayer player) {
        this.player = player;
    }
}
