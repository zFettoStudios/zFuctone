package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public interface MessageDispatcher {
    MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage, @NonNull PlatformPlayer... recipients);
    MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage);
}
