package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

public abstract class ExtendedModule<T> extends SimpleModule<T> {
    protected static final LazyInstance<PermissionRegistry> permissionRegistry = LazyInstance.build(ZFuctone::permissionRegistry);
    protected static final LazyInstance<PlaceholderRegistry> placeholderRegistry = LazyInstance.build(ZFuctone::placeholderRegistry);
    protected static final LazyInstance<CommandRegistry> commandRegistry = LazyInstance.build(ZFuctone::commandRegistry);

    @Override
    public @NonNull T enable() {
        listenerRegistry.get().run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.START));

        if (isEnable()) {
            onEnable();

            registerAll();
            registerListeners();
        }

        listenerRegistry.get().run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T disable() {
        listenerRegistry.get().run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        onDisable();

        unregisterAll();
        unregisterListeners();

        listenerRegistry.get().run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T reload() {
        listenerRegistry.get().run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        unregisterAll();

        onReload();

        if (isEnable()) registerAll();

        listenerRegistry.get().run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    private void registerAll() {
        registerPermissions();
        registerPlaceholders();
        registerCommands();
    }

    private void unregisterAll() {
        unregisterPermissions();
        unregisterPlaceholders();
        unregisterCommands();
    }

    protected void registerPermissions() {
    }
    protected void unregisterPermissions() {
    }

    protected void registerCommands() {
    }
    protected void unregisterCommands() {
    }

    protected void registerPlaceholders() {
    }
    protected void unregisterPlaceholders() {
    }

    protected void registerListeners() {
    }
    protected void unregisterListeners() {
    }
}