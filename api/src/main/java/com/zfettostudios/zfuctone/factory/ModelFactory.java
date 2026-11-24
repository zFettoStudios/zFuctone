package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.DisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.EnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.ReloadEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.model.value.ValueString;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface ModelFactory {
    @NonNull PlaceholderContext buildPlaceholderContext();
    @NonNull PlaceholderContext buildPlaceholderContext(@NonNull String rawtext);

    @NonNull ValueString buildValueString();
    @NonNull ValueString buildValueString(@NonNull String string);
    @NonNull ValueString buildValueString(@NonNull List<String> list);

    @NonNull IntegrationHook<?> emptyIntegrationHook();

    @NonNull PlayerJoinEvent buildPlayerJoinEvent(@NonNull PlatformPlayer player);
    @NonNull PlayerQuitEvent buildPlayerQuitEvent(@NonNull PlatformPlayer player);

    @NonNull EnableEvent buildEnableEvent(@NonNull ZFuctone zfuctone);
    @NonNull DisableEvent buildDisableEvent(@NonNull ZFuctone zfuctone);
    @NonNull ReloadEvent buildReloadEvent(@NonNull ZFuctone zfuctone, @NonNull EventType type);

    @NonNull ModuleReloadEvent buildModuleReloadEvent(@NonNull Module<?> module, @NonNull Class<? extends Module<?>> moduleClass, @NonNull EventType type);
    @NonNull ModuleEnableEvent buildModuleEnableEvent(@NonNull Module<?> module, @NonNull Class<? extends Module<?>> moduleClass, @NonNull EventType type);
    @NonNull ModuleDisableEvent buildModuleDisableEvent(@NonNull Module<?> module, @NonNull Class<? extends Module<?>> moduleClass, @NonNull EventType type);
}
