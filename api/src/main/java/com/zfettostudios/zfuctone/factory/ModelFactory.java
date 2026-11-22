package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.model.value.ValueString;
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
}
