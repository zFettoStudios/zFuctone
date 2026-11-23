package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.DisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.DisableEventImpl;
import com.zfettostudios.zfuctone.model.event.lifecycle.EnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.EnableEventImpl;
import com.zfettostudios.zfuctone.model.event.lifecycle.ReloadEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.ReloadEventImpl;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEventImpl;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEventImpl;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEventImpl;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEventImpl;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEventImpl;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.model.value.PlaceholderContextImpl;
import com.zfettostudios.zfuctone.model.value.ValueString;
import com.zfettostudios.zfuctone.model.value.ValueStringImpl;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.module.integration.IntegrationHookEmpty;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ModelFactoryImpl implements ModelFactory {
    private final LazyInstance<IntegrationHook<?>> emptyIntegrationHook = LazyInstance.build(IntegrationHookEmpty::new);

    @Override
    public @NonNull PlaceholderContext buildPlaceholderContext() {
        return new PlaceholderContextImpl();
    }

    @Override
    public @NonNull PlaceholderContext buildPlaceholderContext(@NonNull String rawtext) {
        return new PlaceholderContextImpl(rawtext);
    }

    @Override
    public @NonNull ValueString buildValueString() {
        return new ValueStringImpl();
    }

    @Override
    public @NonNull ValueString buildValueString(@NonNull String string) {
        return new ValueStringImpl(string);
    }

    @Override
    public @NonNull ValueString buildValueString(@NonNull List<String> list) {
        return new ValueStringImpl(list);
    }

    @Override
    public @NonNull IntegrationHook<?> emptyIntegrationHook() {
        return emptyIntegrationHook.get();
    }

    @Override
    public @NonNull PlayerJoinEvent buildPlayerJoinEvent(@NonNull PlatformPlayer player) {
        return new PlayerJoinEventImpl(player);
    }

    @Override
    public @NonNull PlayerQuitEvent buildPlayerQuitEvent(@NonNull PlatformPlayer player) {
        return new PlayerQuitEventImpl(player);
    }

    @Override
    public @NonNull EnableEvent buildEnableEvent(@NonNull ZFuctone zfuctone) {
        return new EnableEventImpl(zfuctone);
    }

    @Override
    public @NonNull DisableEvent buildDisableEvent(@NonNull ZFuctone zfuctone) {
        return new DisableEventImpl(zfuctone);
    }

    @Override
    public @NonNull ReloadEvent buildReloadEvent(@NonNull ZFuctone zfuctone, @NonNull EventType type) {
        return new ReloadEventImpl(zfuctone, type);
    }

    @Override
    public @NonNull ModuleReloadEvent buildModuleReloadEvent(@NonNull Module<?> module, @NonNull Class<? extends @NonNull Module<?>> moduleClass, @NonNull EventType type) {
        return new ModuleReloadEventImpl(module, moduleClass, type);
    }

    @Override
    public @NonNull ModuleEnableEvent buildModuleEnableEvent(@NonNull Module<?> module, @NonNull Class<? extends @NonNull Module<?>> moduleClass, @NonNull EventType type) {
        return new ModuleEnableEventImpl(module, moduleClass, type);
    }

    @Override
    public @NonNull ModuleDisableEvent buildModuleDisableEvent(@NonNull Module<?> module, @NonNull Class<? extends @NonNull Module<?>> moduleClass, @NonNull EventType type) {
        return new ModuleDisableEventImpl(module, moduleClass, type);
    }
}
