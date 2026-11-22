package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.Event;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListenerRegistryImpl implements ListenerRegistry {
    @Getter
    private final Map<Class<? extends Event>, Map<Event.Priority, Set<Method>>> listeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, Set<ZFListener>> listenersModuleClass = new ConcurrentHashMap<>();
    private final Map<Method, ZFListener> methodOwners = new ConcurrentHashMap<>();

    @Override
    public ListenerRegistry register(@NonNull Class<?> moduleClass, @NonNull ZFListener listener) {
        boolean registeredAny = false;

        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler handler = method.getAnnotation(EventHandler.class);
            if (handler == null) continue;

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) continue;

            Class<? extends Event> eventClass = params[0].asSubclass(Event.class);
            Event.Priority priority = handler.priority();

            method.setAccessible(true);

            Map<Event.Priority, Set<Method>> byPriority = listeners.computeIfAbsent(eventClass,
                _ -> Collections.synchronizedMap(new EnumMap<>(Event.Priority.class)));
            Set<Method> eventMethods = byPriority.computeIfAbsent(priority,
                _ -> ConcurrentHashMap.newKeySet());

            eventMethods.add(method);
            methodOwners.put(method, listener);
            registeredAny = true;
        }

        if (registeredAny)
            listenersModuleClass.computeIfAbsent(moduleClass, _ ->
                ConcurrentHashMap.newKeySet()).add(listener);

        return this;
    }

    @Override
    public ListenerRegistry unregisterAll(@NonNull Class<?> moduleClass) {
        Set<ZFListener> moduleListeners = listenersModuleClass.remove(moduleClass);
        if (moduleListeners == null || moduleListeners.isEmpty()) return this;

        Set<Method> methodsToRemove = ConcurrentHashMap.newKeySet();
        methodOwners.forEach((method, owner) -> {
            if (moduleListeners.contains(owner)) methodsToRemove.add(method);
        });

        for (Method method : methodsToRemove) methodOwners.remove(method);

        for (Map<Event.Priority, Set<Method>> byPriority : listeners.values())
            for (Set<Method> eventMethods : byPriority.values())
                eventMethods.removeAll(methodsToRemove);

        return this;
    }

    @Override
    public ListenerRegistry unregisterAll() {
        listeners.clear();
        listenersModuleClass.clear();
        methodOwners.clear();

        return this;
    }

    @Override
    public @NonNull Set<@NonNull ZFListener> getListeners(@NonNull Class<?> moduleClass) {
        Set<ZFListener> found = listenersModuleClass.get(moduleClass);
        return found == null ? Set.of() : Set.copyOf(found);
    }

    @Override
    public @NonNull Map<Event.@NonNull Priority, @NonNull Set<@NonNull ZFListener>> getListenersByEvent(
        @NonNull Class<? extends Event> eventClass
    ) {
        Map<Event.Priority, Set<Method>> byPriority = listeners.get(eventClass);
        if (byPriority == null || byPriority.isEmpty()) return Map.of();

        Map<Event.Priority, Set<ZFListener>> result = new EnumMap<>(Event.Priority.class);
        synchronized (byPriority) {
            for (Map.Entry<Event.Priority, Set<Method>> entry : byPriority.entrySet()) {
                Set<ZFListener> owners = new CopyOnWriteArraySet<>();
                for (Method method : entry.getValue()) {
                    ZFListener owner = methodOwners.get(method);
                    if (owner != null) owners.add(owner);
                }

                if (!owners.isEmpty()) result.put(entry.getKey(), Collections.unmodifiableSet(owners));
            }
        }

        return result;
    }
}