package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public abstract class SimpleModule<T> implements Module<T> {
    protected final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();

    @Override
    public boolean isEnable() {
        return config().enable();
    }

    @Override
    public boolean isDisable() {
        return !isEnable();
    }

    @Override
    public @NonNull T enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.START));

        T result = onEnable();

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.END));

        return result;
    }

    @Override
    public @NonNull T disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        T result = onDisable();

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        return result;
    }

    @Override
    public @NonNull T reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        T result = onReload();

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        return result;
    }

    protected abstract @NonNull  T onEnable();
    protected abstract @NonNull T onDisable();
    protected abstract @NonNull T onReload();
}