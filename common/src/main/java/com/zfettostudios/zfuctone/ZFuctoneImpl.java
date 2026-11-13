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

        logger.info("Successfully launched!");
        return this;
    }

    @Override
    public ZFuctone disable() {
        logger.info("Disabling...");

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
        return this;
    }

    protected PlatformType detectPlatformType() {
        if (reflectionResolver.hasClass("io.papermc.paper.threadedregions.RegionizedServer")) return PlatformType.FOLIA;
        else if (reflectionResolver.hasClass("io.papermc.paper.ServerBuildInfo") ||
            reflectionResolver.hasClass("com.destroystokyo.paper.ParticleBuilder")) return PlatformType.PAPER;
        else if (reflectionResolver.hasClass("org.bukkit.Bukkit")) return PlatformType.BUKKIT;
        else if (reflectionResolver.hasClass("com.velocitypowered.api.proxy.ProxyServer")) return PlatformType.VELOCITY;
        else if (reflectionResolver.hasClass("net.md_5.bungee.api.ProxyServer")) return PlatformType.BUNGEECORD;
        else if (reflectionResolver.hasClass("org.spongepowered.api.Sponge")) return PlatformType.SPONGE;
        else if (reflectionResolver.hasClass("net.fabricmc.api.ModInitializer")) return PlatformType.FABRIC;
        else if (reflectionResolver.hasClass("net.neoforged.fml.common.Mod")) return PlatformType.NEOFORGE;
        else if (reflectionResolver.hasClass("net.minecraftforge.fml.common.Mod")) return PlatformType.FORGE;

        return PlatformType.BUKKIT;
    }

    protected abstract Path getDataPath();

    protected abstract ComponentLogger getComponentLogger();

    protected abstract Integration createIntegration();
    protected abstract PlatformPlayerAdapter createPlayerAdapter();
    protected abstract PlatformEntityAdapter createEntityAdapter();
    protected abstract PlatformServerAdapter createServerAdapter();
}
