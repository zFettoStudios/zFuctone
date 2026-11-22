package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PlaceholderAPIModule extends IntegrationModule<PlaceholderAPIModule> {
    @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input);

    IntegrationConfig.@NonNull PlaceholderAPI config();

    PermissionConfig.Integration.@NonNull PlaceholderAPI permission();
}
