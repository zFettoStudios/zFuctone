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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class ModuleControllerImpl implements ModuleController {
    @Getter(AccessLevel.NONE)
    private final TaskScheduler taskScheduler = ZFuctoneProvider.get().taskScheduler();

    private final Map<Class<? extends Module<?>>, Module<?>> modules = new ConcurrentHashMap<>();

    @Override
    public @NonNull ModuleController enable() {
        return executeAllModules(Reloadable::enable);
    }

    @Override
    public @NonNull ModuleController disable() {
        return executeAllModules(Reloadable::disable);
    }

    @Override
    public @NonNull ModuleController reload() {
        return executeAllModules(Reloadable::reload);
    }

    private ModuleController executeAllModules(Consumer<Reloadable<?>> consumer) {
        for (Module<?> module : modules.values()) consumer.accept(module);

        return this;
    }

    @Override
    public <T extends Module<T>> @NonNull T register(@NonNull Class<T> moduleClass, Module<T> module) {
        return moduleClass.cast(modules.put(moduleClass, module));
    }

    @Override
    public @Nullable <T extends Module<T>> T unregister(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(modules.remove(moduleClass));
    }

    @Override
    public <T extends Module<T>> T get(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(modules.get(moduleClass));
    }
}