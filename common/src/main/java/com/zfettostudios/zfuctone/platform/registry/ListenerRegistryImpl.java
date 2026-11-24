package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.Event;
import lombok.Getter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListenerRegistryImpl implements ListenerRegistry {
    private static final List<Event.Priority> PRIORITY_ORDER = List.of(
        Event.Priority.LOWEST,
        Event.Priority.LOW,
        Event.Priority.NORMAL,
        Event.Priority.HIGH,
        Event.Priority.HIGHEST,
        Event.Priority.MONITOR
    );

    @Getter
    private final Map<Class<? extends Event<?>>, Map<Event.Priority, Set<Method>>> listeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, Set<ZFListener>> listenersModuleClass = new ConcurrentHashMap<>();
    private final Map<Method, ZFListener> methodOwners = new ConcurrentHashMap<>();

    @Override
    public @NonNull ListenerRegistry register(@NonNull Class<?> moduleClass, @NonNull ZFListener listener) {
        boolean registeredAny = false;

        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler handler = method.getAnnotation(EventHandler.class);
            if (handler == null) continue;

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) continue;

            Class<? extends Event<?>> eventClass = (Class<? extends Event<?>>) params[0];
            Event.Priority priority = handler.priority();

            method.setAccessible(true);

            Map<Event.Priority, Set<Method>> byPriority = listeners.computeIfAbsent(eventClass,
                _ -> new EnumMap<>(Event.Priority.class));
            Set<Method> eventMethods = byPriority.computeIfAbsent(priority,
                _ -> new CopyOnWriteArraySet<>());

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
    public @NonNull ListenerRegistry unregisterAll(@NonNull Class<?> moduleClass) {
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
    public @NonNull ListenerRegistry unregisterAll() {
        listeners.clear();
        listenersModuleClass.clear();
        methodOwners.clear();

        return this;
    }

    @Override
    public <T extends Event<T>> @NonNull ListenerRegistry run(@NonNull Class<T> eventClass, @NonNull T event) {
        Map<Event.Priority, Set<Method>> byPriority = listeners.get(eventClass);
        if (byPriority == null || byPriority.isEmpty()) return this;

        for (Event.Priority priority : PRIORITY_ORDER) {
            Set<Method> methods = byPriority.get(priority);
            if (methods == null || methods.isEmpty()) continue;

            for (Method method : methods) {
                ZFListener owner = methodOwners.get(method);
                if (owner == null) continue;

                EventHandler handler = method.getAnnotation(EventHandler.class);
                if (handler == null || !handler.ignoreCancelled() && event.cancelled()) continue;

                try {
                    method.invoke(owner, event);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Failed to invoke event handler " + method.getName() + " for " + eventClass.getSimpleName(), e);
                }
            }
        }

        return this;
    }

    @Override
    public @Nullable Set<ZFListener> getListeners(@NonNull Class<?> moduleClass) {
        return listenersModuleClass.get(moduleClass);
    }

    @Override
    public @Nullable Map<Event.Priority, Set<Method>> getListenersByEvent(@NonNull Class<? extends Event<?>> eventClass) {
        return listeners.get(eventClass);
    }
}