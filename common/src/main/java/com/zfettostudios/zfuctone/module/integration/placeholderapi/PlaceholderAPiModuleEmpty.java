package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PlaceholderAPiModuleEmpty extends PlaceholderAPIModuleImpl {
    @Override
    public @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input) {
        return input;
    }
}
