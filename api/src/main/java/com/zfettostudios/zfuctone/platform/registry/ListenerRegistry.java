package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.event.Event;
import org.jspecify.annotations.NonNull;

public interface ListenerRegistry {
    <T extends Event> T register(@NonNull String name, @NonNull T event);
    ListenerRegistry unregister(@NonNull String name);
}
