package com.zfettostudios.zfuctone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcherImpl;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.file.FileFacadeImpl;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.logging.ZLoggerImpl;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.command.CommandModuleController;
import com.zfettostudios.zfuctone.module.command.CommandModuleControllerImpl;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.module.message.MessageModuleController;
import com.zfettostudios.zfuctone.module.message.MessageModuleControllerImpl;
import com.zfettostudios.zfuctone.platform.adapter.PlatformEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.service.PlatformPlayerServiceImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ZFuctoneImpl implements ZFuctone {
    @Accessors(fluent = false)
    PlatformType platformType = detectPlatformType();
    FileFacade fileFacade;
    ZLogger logger;
    TaskScheduler taskScheduler;
    PlatformPlayerService playerService;
    PlatformPlayerAdapter playerAdapter;
    PlatformServerAdapter serverAdapter;
    PlatformEntityAdapter entityAdapter;
    Integration integration;
    CommandRegistry commandRegistry;
    MessageDispatcher messageDispatcher;
    MessageModuleController messageModuleController;
    CommandModuleController commandModuleController;

    public ZFuctoneImpl() {
        ZFuctoneAPI.setInstance(this);

        playerAdapter = createPlayerAdapter();
        serverAdapter = createServerAdapter();
        entityAdapter = createEntityAdapter();

        logger = new ZLoggerImpl(getComponentLogger());
        fileFacade = new FileFacadeImpl(getDataPath());
        taskScheduler = new TaskSchedulerImpl(((FileFacadeImpl) fileFacade).executor());

        integration = createIntegration();
        playerService = new PlatformPlayerServiceImpl();
        messageDispatcher = new MessageDispatcherImpl();
        commandRegistry = createCommandRegistry();

        messageModuleController = new MessageModuleControllerImpl();
        commandModuleController = new CommandModuleControllerImpl();
    }

    @Override
    public ZFuctone enable() {
        fileFacade.enable();
        taskScheduler.start();

        messageModuleController.enable();
        commandModuleController.enable();

        logger.info("Successfully launched!");
        return this;
    }

    @Override
    public ZFuctone disable() {
        logger.info("Disabling...");

        messageModuleController.disable();
        commandModuleController.disable();

        taskScheduler.shutdown();
        fileFacade.disable();
        return this;
    }

    @Override
    public ZFuctone reload() {
        fileFacade.updateFiles();
        taskScheduler.reload();
        return this;
    }

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

    protected abstract Path getDataPath();

    protected abstract ComponentLogger getComponentLogger();

    protected abstract Integration createIntegration();
    protected abstract CommandRegistry createCommandRegistry();
    protected abstract PlatformPlayerAdapter createPlayerAdapter();
    protected abstract PlatformEntityAdapter createEntityAdapter();
    protected abstract PlatformServerAdapter createServerAdapter();
}
