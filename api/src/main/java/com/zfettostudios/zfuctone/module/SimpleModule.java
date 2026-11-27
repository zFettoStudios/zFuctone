package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

public abstract class SimpleModule<T> implements Module<T> {
    protected static final LazyInstance<ListenerRegistry> listenerRegistry = LazyInstance.build(ZFuctone::listenerRegistry);

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
        listenerRegistry.get().run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.START));

        onEnable();

        listenerRegistry.get().run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T disable() {
        listenerRegistry.get().run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        onDisable();

        listenerRegistry.get().run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T reload() {
        listenerRegistry.get().run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        onReload();

        listenerRegistry.get().run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    protected void onEnable() {
    }
    protected void onDisable() {
    }
    protected void onReload() {
        onDisable();
        if (isEnable()) onEnable();
    }
}