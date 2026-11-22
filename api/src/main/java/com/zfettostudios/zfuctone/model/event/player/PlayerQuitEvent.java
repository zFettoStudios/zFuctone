package com.zfettostudios.zfuctone.model.event.player;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface PlayerQuitEvent extends PlayerEvent {
    static @NonNull PlayerQuitEvent build(@NonNull PlatformPlayer player) {
        return FactoryProvider.modelFactory().buildPlayerQuitEvent(player);
    }
}
