package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public abstract class ExtendedModule<T> extends SimpleModule<T> {
    protected static final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();
    protected static final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    protected static final CommandRegistry commandRegistry = ZFuctoneProvider.get().commandRegistry();

    @Override
    public @NonNull T enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.START));

        if (isEnable()) {
            onEnable();

            registerAll();
            registerListeners();
        }

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, moduleClass(), EventType.END));

        return get();
    }

    @Override
    public @NonNull T disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        onDisable();

        unregisterAll();
        unregisterListeners();

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, moduleClass(), EventType.START));

        return get();
    }

    @Override
    public @NonNull T reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

        unregisterAll();

        onReload();

        if (isEnable()) registerAll();

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, moduleClass(), EventType.START));

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