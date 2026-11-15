package com.zfettostudios.zfuctone.logging;

import net.kyori.adventure.text.Component;

public interface ZLogger {
    void logEnabling();
    void logEnabled();

    void logDisabling();
    void logDisabled();

    void logReloading();
    void logReloaded();

    void logDescription();

    ZLogger warn(String format, Object... arguments);
    ZLogger warn(String message);
    ZLogger warn(Component component);

    ZLogger info(String format, Object... arguments);
    ZLogger info(String message);
    ZLogger info(Component message);
}
