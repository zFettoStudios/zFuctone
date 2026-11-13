package com.zfettostudios.zfuctone.module.integration.placeholdersapi;

import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PlaceholderAPIModule extends IntegrationModule<PlaceholderAPIModule> {
    @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input);
}
