package com.zfettostudios.zfuctone.model.event.player;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

@Getter
@Setter
@Accessors(fluent = true)
public class PlayerQuitEventImpl implements PlayerQuitEvent {
    private final Runnable removeQuitMessage;
    private PlatformPlayer player;
    private boolean cancelled = false;

    public PlayerQuitEventImpl(PlatformPlayer player, Runnable removeQuitMessage) {
        this.player = player;
        this.removeQuitMessage = removeQuitMessage;
    }

    @Override
    public @NonNull PlayerQuitEvent removeQuitMessage() {
        removeQuitMessage.run();
        return this;
    }
}
