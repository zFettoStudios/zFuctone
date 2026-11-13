package com.zfettostudios.zfuctone;

import lombok.Getter;
import lombok.experimental.Accessors;

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
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistryImpl;
import com.zfettostudios.zfuctone.reflect.ReflectionResolver;
import com.zfettostudios.zfuctone.reflect.ReflectionResolverImpl;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.service.PlatformPlayerServiceImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public abstract class ZFuctoneImpl implements ZFuctone {
    @Accessors(fluent = false)
    protected final PlatformType platformType;
    protected final FileFacade fileFacade;
    protected final ZLogger logger;
    protected final TaskScheduler taskScheduler;
    protected final PlatformPlayerService playerService;
    protected final PlatformPlayerAdapter playerAdapter;
    protected final PlatformServerAdapter serverAdapter;
    protected final PlatformEntityAdapter entityAdapter;
    protected final Integration integration;
    protected final MessageDispatcher messageDispatcher;
    protected final MessageModuleController messageModuleController;
    protected final CommandModuleController commandModuleController;
    protected final ReflectionResolver reflectionResolver;
    protected final ListenerRegistry listenerRegistry;

    public ZFuctoneImpl() {
        ZFuctoneAPI.setInstance(this);

        reflectionResolver = new ReflectionResolverImpl();
        platformType = detectPlatformType();

        playerAdapter = createPlayerAdapter();
        serverAdapter = createServerAdapter();
        entityAdapter = createEntityAdapter();

        logger = new ZLoggerImpl(getComponentLogger());
        fileFacade = new FileFacadeImpl(getDataPath());
        taskScheduler = new TaskSchedulerImpl(((FileFacadeImpl) fileFacade).executor());

        listenerRegistry = new ListenerRegistryImpl();

        integration = createIntegration();
        playerService = new PlatformPlayerServiceImpl();
        messageDispatcher = new MessageDispatcherImpl();

        messageModuleController = new MessageModuleControllerImpl();
        commandModuleController = new CommandModuleControllerImpl();
    }

    @Override
    public ZFuctone enable() {
        fileFacade.enable();
        taskScheduler.start();

        integration.enable();
        playerService.enable();

        messageModuleController.enable();
        commandModuleController.enable();

        registerListeners();

        logger.info("Successfully launched!");
        return this;
    }

    @Override
    public ZFuctone disable() {
        logger.info("Disabling...");

        unregisterListeners();

        messageModuleController.disable();
        commandModuleController.disable();

        playerService.disable();

        integration.disable();

        fileFacade.disable();
        taskScheduler.shutdown();
        return this;
    }

    @Override
    public ZFuctone reload() {
        fileFacade.updateFiles();
        taskScheduler.reload();

        integration.disable();
        integration.enable();

        playerService.disable();
        playerService.enable();

        messageModuleController.disable();
        messageModuleController.enable();

        commandModuleController.disable();
        commandModuleController.enable();

        unregisterListeners();
        registerListeners();
        return this;
    }

    protected abstract PlatformType detectPlatformType();

//     if (reflectionResolver.hasClass("com.velocitypowered.api.proxy.ProxyServer")) return PlatformType.VELOCITY;
//     if (reflectionResolver.hasClass("net.md_5.bungee.api.ProxyServer")) return PlatformType.BUNGEECORD;
//     if (reflectionResolver.hasClass("org.spongepowered.api.Sponge")) return PlatformType.SPONGE;
//     if (reflectionResolver.hasClass("net.fabricmc.api.ModInitializer")) return PlatformType.FABRIC;
//     if (reflectionResolver.hasClass("net.neoforged.fml.common.Mod")) return PlatformType.NEOFORGE;
//     if (reflectionResolver.hasClass("net.minecraftforge.fml.common.Mod")) return PlatformType.FORGE;

    protected abstract Path getDataPath();

    protected abstract ComponentLogger getComponentLogger();

    protected abstract Integration createIntegration();
    protected abstract PlatformPlayerAdapter createPlayerAdapter();
    protected abstract PlatformEntityAdapter createEntityAdapter();
    protected abstract PlatformServerAdapter createServerAdapter();

    protected abstract void registerListeners();
    protected abstract void unregisterListeners();
}
