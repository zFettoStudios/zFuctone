package com.zfettostudios.zfuctone.logging;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
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
        return ZFuctoneProvider.get().fileFacade().config().logger();
    }

    @Override
    public ZLogger logEnabling() {
        return info("Enabling...");
    }

    @Override
    public ZLogger logEnabled() {
        return info("{} v{} enabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logDisabling() {
        return info("Disabling...");
    }

    @Override
    public ZLogger logDisabled() {
        return info("{} v{} disabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logReloading() {
        return info("Reloading...");
    }

    @Override
    public ZLogger logReloaded() {
        return info("{} v{} reloaded", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logDescription() {
        config().description().forEach(string ->
            info(string.replace("<version>", BuildConstant.PROJECT_VERSION)));

        return this;
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
