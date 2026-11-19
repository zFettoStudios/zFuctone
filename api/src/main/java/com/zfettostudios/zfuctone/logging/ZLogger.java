package com.zfettostudios.zfuctone.logging;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

public interface ZLogger {
    ZLogger logEnabling();
    ZLogger logEnabled();

    ZLogger logDisabling();
    ZLogger logDisabled();

    ZLogger logReloading();
    ZLogger logReloaded();

    ZLogger logDescription();

    ZLogger warn(@NonNull String format, @NonNull Object... arguments);
    ZLogger warn(@NonNull String message);
    ZLogger warn(@NonNull Component component);

    ZLogger info(@NonNull String format, @NonNull Object... arguments);
    ZLogger info(@NonNull String message);
    ZLogger info(@NonNull Component message);
}
