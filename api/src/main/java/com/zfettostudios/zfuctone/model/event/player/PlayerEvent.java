package com.zfettostudios.zfuctone.model.event.player;

import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

public interface PlayerEvent extends Event {
    PlatformPlayer player();

    Event player(@NonNull PlatformPlayer player);
}
