package com.zfettostudios.zfuctone.model.event.player;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

@Getter
@Setter
@Accessors(fluent = true)
public class PlayerJoinEventImpl implements PlayerJoinEvent {
    private final Runnable removeJoinMessage;
    private PlatformPlayer player;
    private boolean cancelled = false;

    public PlayerJoinEventImpl(PlatformPlayer player, Runnable removeJoinMessage) {
        this.player = player;
        this.removeJoinMessage = removeJoinMessage;
    }

    @Override
    public @NonNull PlayerJoinEvent removeJoinMessage() {
        removeJoinMessage.run();
        return this;
    }
}