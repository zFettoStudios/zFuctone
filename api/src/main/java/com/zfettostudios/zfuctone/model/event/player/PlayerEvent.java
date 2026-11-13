package com.zfettostudios.zfuctone.model.event.player;

import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;

public interface PlayerEvent extends Event {
    PlatformPlayer player();

    <T extends PlayerEvent> T player(PlatformPlayer player);
}
