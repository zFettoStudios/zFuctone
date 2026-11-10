package com.zfettostudios.zfuctone.logging;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;

@RequiredArgsConstructor
public class ZLoggerImpl implements ZLogger {
    ComponentLogger logger;
    MiniMessage mm = MiniMessage.miniMessage();

    private Config.Logger config() {
        return ZFuctoneAPI.get().fileFacade().config().logger();
    }

    @Override
    public ZLogger warn(String format, Object... arguments) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + format), arguments);
        return this;
    }

    @Override
    public ZLogger warn(String message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + message));
        return this;
    }

    @Override
    public ZLogger warn(Component message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn()).append(message));
        return this;
    }

    @Override
    public ZLogger info(String format, Object... arguments) {
        logger.info(mm.deserialize(config().prefix() + config().info() + format), arguments);
        return this;
    }

    @Override
    public ZLogger info(String message) {
        logger.info(mm.deserialize(config().prefix() + config().info() + message));
        return this;
    }

    @Override
    public ZLogger info(Component message) {
        logger.info(mm.deserialize(config().prefix() + config().info()).append(message));
        return this;
    }
}
