package com.zfettostudios.zfuctone.module.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class ModuleControllerImpl implements ModuleController {
    @Getter(AccessLevel.NONE)
    private final TaskScheduler taskScheduler = ZFuctoneProvider.get().taskScheduler();

    private final Map<Class<? extends Module<?>>, Module<?>> modules = new ConcurrentHashMap<>();
    private final List<Class<? extends Module<?>>> registrationOrder = new CopyOnWriteArrayList<>();

    @Override
    public @NonNull ModuleController enable() {
        return executeModuleAll(Reloadable::enable);
    }

    @Override
    public @NonNull ModuleController disable() {
        return executeModuleAll(Reloadable::enable);
    }

    @Override
    public @NonNull ModuleController reload() {
        return executeModuleAll(Reloadable::reload);
    }

    private ModuleController executeModuleAll(Consumer<Module<?>> consumer) {
        for (Class<? extends Module<?>> moduleClass : registrationOrder) {
            Module<?> module = modules.get(moduleClass);
            if (module != null) consumer.accept(module);
        }

        return this;
    }

    @Override
    public <T extends Module<T>> @NonNull T register(@NonNull Class<T> moduleClass, @NonNull Module<T> module) {
        if (modules.containsKey(moduleClass)) return module.get();

        modules.put(moduleClass, module);
        registrationOrder.add(moduleClass);

        return module.get();
    }

    @Override
    public @Nullable <T extends Module<T>> T unregister(@NonNull Class<T> moduleClass) {
        registrationOrder.remove(moduleClass);
        return moduleClass.cast(modules.remove(moduleClass));
    }

    @Override
    public <T extends Module<T>> T get(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(modules.get(moduleClass));
    }

    @Override
    public @NonNull Map<Class<? extends Module<?>>, Module<?>> modules() {
        return Collections.unmodifiableMap(modules);
    }
}
