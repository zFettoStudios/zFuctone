package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.Event;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public interface ListenerRegistry {
    ListenerRegistry register(@NonNull Class<?> moduleClass, @NonNull ZFListener listener);

    ListenerRegistry unregisterAll(@NonNull Class<?> moduleClass);
    ListenerRegistry unregisterAll();

    @NonNull Set<@NonNull ZFListener> getListeners(@NonNull Class<?> moduleClass);
    @NonNull Map<Event.@NonNull Priority, @NonNull Set<@NonNull ZFListener>> getListenersByEvent(@NonNull Class<? extends Event> eventClass);
    @NonNull Map<@NonNull Class<? extends Event> , @NonNull Map<Event.@NonNull Priority, Set<@NonNull Method>>> getListeners();
}