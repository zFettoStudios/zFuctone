package com.zfettostudios.zfuctone.model.event.player;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface PlayerJoinEvent extends PlayerEvent<PlayerJoinEvent> {
    static @NonNull PlayerJoinEvent build(@NonNull PlatformPlayer player) {
        return FactoryProvider.modelFactory().buildPlayerJoinEvent(player);
    }
}
