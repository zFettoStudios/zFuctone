package com.zfettostudios.zfuctone.logging;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

public interface ZLogger extends PlatformPlayer {
    @NonNull ZLogger logEnabling();
    @NonNull ZLogger logEnabled();

    @NonNull ZLogger logDisabling();
    @NonNull ZLogger logDisabled();


    @NonNull ZLogger logReloading();
    @NonNull ZLogger logReloaded();


    @NonNull ZLogger logDescription();

    @NonNull ZLogger warn(@NonNull String format, @NonNull Object... arguments);
    @NonNull ZLogger warn(@NonNull String message);
    @NonNull ZLogger warn(@NonNull Component component);

    @NonNull ZLogger info(@NonNull String format, @NonNull Object... arguments);
    @NonNull ZLogger info(@NonNull String message);
    @NonNull ZLogger info(@NonNull Component message);
}