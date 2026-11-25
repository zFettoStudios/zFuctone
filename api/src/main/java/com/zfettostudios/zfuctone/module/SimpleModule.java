package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public abstract class SimpleModule<T> implements Module<T> {
    protected static final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();

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

        onEnable();

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        onDisable();

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        return get();
    }

    @Override
    public @NonNull T reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        onReload();

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        return get();
    }

    protected void onEnable() {
    }
    protected void onDisable() {
    }
    protected void onReload() {
        if (isEnable()) onEnable();
        onDisable();
    }
}