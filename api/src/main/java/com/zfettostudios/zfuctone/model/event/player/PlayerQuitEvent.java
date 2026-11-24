package com.zfettostudios.zfuctone.model.event.player;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface PlayerQuitEvent extends PlayerEvent<PlayerQuitEvent> {
    static @NonNull PlayerQuitEvent build(@NonNull PlatformPlayer player, @NonNull Runnable removeJoinMessage) {
        return FactoryProvider.modelFactory().buildPlayerQuitEvent(player, removeJoinMessage);
    }

    @NonNull PlayerQuitEvent removeQuitMessage();
}
