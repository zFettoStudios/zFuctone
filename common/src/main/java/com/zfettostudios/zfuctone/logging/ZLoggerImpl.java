package com.zfettostudios.zfuctone.logging;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;

public class ZLoggerImpl implements ZLogger {
    private final ComponentLogger logger = ComponentLogger.logger("");
    private final MiniMessage mm = MiniMessage.miniMessage();

    private Config.Logger config() {
        return ZFuctoneAPI.get().fileFacade().config().logger();
    }

    @Override
    public void logEnabling() {
        info("Enabling...");
    }

    @Override
    public void logEnabled() {
        info("{} v{} enabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public void logDisabling() {
        info("Disabling...");
    }

    @Override
    public void logDisabled() {
        info("{} v{} disabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public void logReloading() {
        info("Reloading...");
    }

    @Override
    public void logReloaded() {
        info("{} v{} reloaded", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public void logDescription() {
        config().description().forEach(string ->
            info(string.replace("<version>", BuildConstant.PROJECT_VERSION)));
    }

    @Override
    public ZLogger warn(@NonNull String format, Object... arguments) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + format), arguments);
        return this;
    }

    @Override
    public ZLogger warn(@NonNull String message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + message));
        return this;
    }

    @Override
    public ZLogger warn(@NonNull Component message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn()).append(message));
        return this;
    }

    @Override
    public ZLogger info(@NonNull String format, Object... arguments) {
        logger.info(mm.deserialize(config().prefix() + config().info() + format), arguments);
        return this;
    }

    @Override
    public ZLogger info(@NonNull String message) {
        logger.info(mm.deserialize(config().prefix() + config().info() + message));
        return this;
    }

    @Override
    public ZLogger info(@NonNull Component message) {
        logger.info(mm.deserialize(config().prefix() + config().info()).append(message));
        return this;
    }
}
