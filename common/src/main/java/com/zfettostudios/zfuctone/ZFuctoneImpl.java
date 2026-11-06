package com.zfettostudios.zfuctone;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcherImpl;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.file.FileFacadeImpl;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.logging.ZLoggerImpl;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.command.zfuctone.ZFuctoneCommandModule;
import com.zfettostudios.zfuctone.module.command.zfuctone.ZFuctoneCommandModuleImpl;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.service.PlatformPlayerServiceImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.nio.file.Path;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ZFuctoneImpl implements ZFuctone {
    PlatformType platformType = detectPlatformType();
    FileFacade fileFacade;
    TaskScheduler scheduler;
    ZLogger logger;
    PlatformPlayerService playerService;
    MessageDispatcher messageDispatcher;

    @NonFinal ZFuctoneCommandModule zfuctoneCommand;

    public ZFuctoneImpl() {
        ZFuctoneAPI.setInstance(this);

        fileFacade = new FileFacadeImpl(getDataPath());
        logger = new ZLoggerImpl(getComponentLogger());
        scheduler = new TaskSchedulerImpl(((FileFacadeImpl) fileFacade).executor());
        playerService = new PlatformPlayerServiceImpl();
        messageDispatcher = new MessageDispatcherImpl();
    }

    @Override
    public <T> T get(Class<T> type) {
        if (type.isInstance(platformType)) return type.cast(platformType);
        else if (type.isInstance(fileFacade)) return type.cast(fileFacade);
        else if (type.isInstance(scheduler)) return type.cast(scheduler);
        else if (type.isInstance(logger)) return type.cast(logger);
        else if (type.isInstance(messageDispatcher)) return type.cast(messageDispatcher);

        return platformGet(type);
    }

    @Override
    public ZFuctone enable() {
        fileFacade.enable();
        scheduler.start();

        platformEnable();

        zfuctoneCommand = new ZFuctoneCommandModuleImpl();
        zfuctoneCommand.register();

        logger.info("Successfully launched!");
        return this;
    }

    @Override
    public ZFuctone disable() {
        logger.info("Disabling...");

        zfuctoneCommand.unregister();

        platformDisable();
        scheduler.shutdown();
        fileFacade.disable();
        return this;
    }

    @Override
    public ZFuctone reload() {
        fileFacade.updateFiles();
        scheduler.reload();
        return platformReload();
    }

    protected abstract void platformEnable();
    protected abstract void platformDisable();
    protected abstract ZFuctone platformReload();

    protected PlatformType detectPlatformType() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return PlatformType.PAPER;
        } catch (ClassNotFoundException _) {
            try {
                Class.forName("org.bukkit.Bukkit");
                return PlatformType.BUKKIT;
            } catch (ClassNotFoundException _) {
                return PlatformType.VELOCITY;
            }
        }
    }

    protected abstract <T> T platformGet(Class<T> type);

    protected abstract Path getDataPath();

    protected abstract ComponentLogger getComponentLogger();
}
