package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.event.Event;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListenerRegistryImpl implements ListenerRegistry {
    private final Map<String, Event> events = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> T register(@NonNull String name, @NonNull T event) {
        return event;
    }

    @Override
    public ListenerRegistry unregister(@NonNull String name) {
        return this;
    }
}
