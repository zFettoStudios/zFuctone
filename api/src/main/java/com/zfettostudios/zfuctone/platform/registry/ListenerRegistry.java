package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.Event;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public interface ListenerRegistry {
    @NonNull ListenerRegistry register(@NonNull Class<?> moduleClass, @NonNull ZFListener listener);

    @NonNull ListenerRegistry unregisterAll(@NonNull Class<?> moduleClass);
    @NonNull ListenerRegistry unregisterAll();

    @NonNull <T extends Event<T>> ListenerRegistry run(@NonNull Class<T> eventClass, @NonNull T event);

    @Nullable Set<ZFListener> getListeners(@NonNull Class<?> moduleClass);
    @Nullable Map<Event.Priority, Set<Method>> getListenersByEvent(@NonNull Class<? extends Event<?>> eventClass);
    @NonNull Map<Class<? extends Event<?>>, Map<Event.Priority, Set<Method>>> getListeners();
}