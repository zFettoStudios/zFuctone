package com.zfettostudios.zfuctone;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcherImpl;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.file.FileFacadeImpl;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.logging.ZLoggerImpl;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.controller.ModuleControllerImpl;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModuleImpl;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModuleImpl;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.CustomPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.CustomPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.PlayerPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.PlayerPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.ProjectPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.ProjectPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.ServerPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.ServerPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistryImpl;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistryImpl;
import com.zfettostudios.zfuctone.provider.BuilderProviderImpl;
import com.zfettostudios.zfuctone.resolver.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.service.PlatformPlayerServiceImpl;

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
    protected final MessageDispatcher messageDispatcher;
    protected final ReflectionResolver reflectionResolver;
    protected final ModuleController moduleController;
    protected final LibraryResolver libraryResolver;
    protected final PermissionRegistry permissionRegistry;
    protected final PlaceholderRegistry placeholderRegistry;

    public ZFuctoneImpl(ReflectionResolver reflectionResolver, PlatformType platformType, LibraryResolver libraryResolver) {
        ZFuctoneAPIImpl.set(this);

        this.reflectionResolver = reflectionResolver;
        this.platformType = platformType;
        this.libraryResolver = libraryResolver;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(false).debug(false);

        playerAdapter = createPlayerAdapter();
        serverAdapter = createServerAdapter();

        logger = new ZLoggerImpl();
        fileFacade = new FileFacadeImpl(getDataPath());
        taskScheduler = new TaskSchedulerImpl(((FileFacadeImpl) fileFacade).executor());

        permissionRegistry = new PermissionRegistryImpl();
        placeholderRegistry = new PlaceholderRegistryImpl();

        playerService = new PlatformPlayerServiceImpl();
        messageDispatcher = new MessageDispatcherImpl();

        moduleController = new ModuleControllerImpl();
    }

    @Override
    public ZFuctone enable() {
        logger.logEnabling();

        fileFacade.enable();
        taskScheduler.start();
        registerTaskScheduler();

        logger.logDescription();

        playerService.enable();

        addedModules();
        addedPlatformModules();
        moduleController.enable();

        BuilderProviderImpl.init();

        registerListeners();

        logger.logEnabled();
        return this;
    }

    @Override
    public ZFuctone disable() {
        logger.logDisabling();

        unregisterListeners();

        moduleController.disable();

        playerService.disable();

        fileFacade.disable();
        libraryResolver.disable();

        taskScheduler.shutdown();

        logger.logDisabled();
        return this;
    }

    @Override
    public ZFuctone reload() {
        logger.logReloading();

        fileFacade.reload();
        taskScheduler.reload();

        playerService.reload();

        moduleController.reload();

        logger.logReloaded();
        return this;
    }

    private void addedModules() {
        GeyserModule geyserModule = new GeyserModuleImpl();

        moduleController.register(ViaVersionModule.class, new ViaVersionModuleImpl());
        moduleController.register(GeyserModule.class, geyserModule);
        moduleController.register(FloodgateModule.class, new FloodgateModuleImpl(geyserModule));
        moduleController.register(LuckPermsModule.class, new LuckPermsModuleImpl());

        moduleController.register(ProjectPlaceholderModule.class, new ProjectPlaceholderModuleImpl());
        moduleController.register(PlayerPlaceholderModule.class, new PlayerPlaceholderModuleImpl());
        moduleController.register(ServerPlaceholderModule.class, new ServerPlaceholderModuleImpl());
        moduleController.register(CustomPlaceholderModule.class, new CustomPlaceholderModuleImpl());
    }

    protected abstract void addedPlatformModules();

//     if (reflectionResolver.hasClass("com.velocitypowered.api.proxy.ProxyServer")) return PlatformType.VELOCITY;
//     if (reflectionResolver.hasClass("net.md_5.bungee.api.ProxyServer")) return PlatformType.BUNGEECORD;
//     if (reflectionResolver.hasClass("org.spongepowered.api.Sponge")) return PlatformType.SPONGE;
//     if (reflectionResolver.hasClass("net.fabricmc.api.ModInitializer")) return PlatformType.FABRIC;
//     if (reflectionResolver.hasClass("net.neoforged.fml.common.Mod")) return PlatformType.NEOFORGE;
//     if (reflectionResolver.hasClass("net.minecraftforge.fml.common.Mod")) return PlatformType.FORGE;

    protected abstract Path getDataPath();

    protected abstract PlatformPlayerAdapter createPlayerAdapter();
    protected abstract PlatformServerAdapter createServerAdapter();

    protected abstract void registerListeners();
    protected abstract void unregisterListeners();

    protected abstract void registerTaskScheduler();
}
