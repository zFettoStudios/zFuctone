package com.zfettostudios.zfuctone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcherImpl;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.file.FileFacadeImpl;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.logging.ZLoggerImpl;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.model.event.lifecycle.DisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.EnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.ReloadEvent;
import com.zfettostudios.zfuctone.module.command.zfuctone.ZFuctoneCommandModule;
import com.zfettostudios.zfuctone.module.command.zfuctone.ZFuctoneCommandModuleImpl;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.controller.ModuleControllerImpl;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModuleImpl;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModuleImpl;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModuleImpl;
import com.zfettostudios.zfuctone.module.message.join.JoinModule;
import com.zfettostudios.zfuctone.module.message.join.JoinModuleImpl;
import com.zfettostudios.zfuctone.module.message.quit.QuitModule;
import com.zfettostudios.zfuctone.module.message.quit.QuitModuleImpl;
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
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistryImpl;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistryImpl;
import com.zfettostudios.zfuctone.provider.FactoryProviderImpl;
import com.zfettostudios.zfuctone.provider.ZFuctoneProviderImpl;
import com.zfettostudios.zfuctone.resolver.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.service.PlatformPlayerServiceImpl;
import org.jspecify.annotations.NonNull;

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
    protected final ListenerRegistry listenerRegistry;
    protected final CommandRegistry commandRegistry;

    @Getter(AccessLevel.NONE)
    protected final Builder builder;

    public ZFuctoneImpl(ReflectionResolver reflectionResolver, PlatformType platformType, LibraryResolver libraryResolver) {
        ZFuctoneProviderImpl.set(this);
        FactoryProviderImpl.init();

        this.reflectionResolver = reflectionResolver;
        this.platformType = platformType;
        this.libraryResolver = libraryResolver;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(false).debug(false);

        builder = builder();

        playerAdapter = builder.buildPlayerAdapter();
        serverAdapter = builder.buildServerAdapter();

        logger = new ZLoggerImpl();
        fileFacade = new FileFacadeImpl(getDataPath());
        taskScheduler = new TaskSchedulerImpl(((FileFacadeImpl) fileFacade).executor());

        permissionRegistry = builder.buildPermissionRegistry();
        placeholderRegistry = new PlaceholderRegistryImpl();
        listenerRegistry = new ListenerRegistryImpl();
        commandRegistry = builder.buildCommandRegistry();

        playerService = new PlatformPlayerServiceImpl();
        messageDispatcher = new MessageDispatcherImpl();

        moduleController = new ModuleControllerImpl();
    }

    @Override
    public @NonNull ZFuctone enable() {
        fileFacade.enable();

        logger.logEnabling();

        taskScheduler.start();
        registerTaskScheduler();

        logger.logDescription();

        playerService.enable();

        registerModules();
        moduleController.enable();

        registerListeners();

        logger.logEnabled();

        listenerRegistry.run(EnableEvent.class, EnableEvent.build(this));

        return this;
    }

    @Override
    public @NonNull ZFuctone disable() {
        listenerRegistry.run(DisableEvent.class, DisableEvent.build(this));

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
    public @NonNull ZFuctone reload() {
        listenerRegistry.run(ReloadEvent.class, ReloadEvent.build(this, EventType.START));

        fileFacade.reload();

        logger.logReloading();

        taskScheduler.reload();

        playerService.reload();

        moduleController.reload();

        logger.logReloaded();

        listenerRegistry.run(ReloadEvent.class, ReloadEvent.build(this, EventType.END));

        return this;
    }

//     if (reflectionResolver.hasClass("com.velocitypowered.api.proxy.ProxyServer")) return PlatformType.VELOCITY;
//     if (reflectionResolver.hasClass("net.md_5.bungee.api.ProxyServer")) return PlatformType.BUNGEECORD;
//     if (reflectionResolver.hasClass("org.spongepowered.api.Sponge")) return PlatformType.SPONGE;
//     if (reflectionResolver.hasClass("net.fabricmc.api.ModInitializer")) return PlatformType.FABRIC;
//     if (reflectionResolver.hasClass("net.neoforged.fml.common.Mod")) return PlatformType.NEOFORGE;
//     if (reflectionResolver.hasClass("net.minecraftforge.fml.common.Mod")) return PlatformType.FORGE;

    protected abstract Path getDataPath();

    protected abstract void registerListeners();
    protected abstract void unregisterListeners();

    protected abstract void registerTaskScheduler();

    protected void registerModules() {
        moduleController.register(ViaVersionModule.class, new ViaVersionModuleImpl());
        moduleController.register(GeyserModule.class, new GeyserModuleImpl());
        moduleController.register(FloodgateModule.class, new FloodgateModuleImpl());
        moduleController.register(LuckPermsModule.class, new LuckPermsModuleImpl());
        moduleController.register(MiniPlaceholdersModule.class, builder.buildMiniPlaceholdersModule());
        moduleController.register(PlaceholderAPIModule.class, builder.buildPlaceholderAPIModule());

        moduleController.register(ProjectPlaceholderModule.class, new ProjectPlaceholderModuleImpl());
        moduleController.register(PlayerPlaceholderModule.class, new PlayerPlaceholderModuleImpl());
        moduleController.register(ServerPlaceholderModule.class, new ServerPlaceholderModuleImpl());
        moduleController.register(CustomPlaceholderModule.class, new CustomPlaceholderModuleImpl());

        moduleController.register(JoinModule.class, new JoinModuleImpl());
        moduleController.register(QuitModule.class, new QuitModuleImpl());

        moduleController.register(ZFuctoneCommandModule.class, new ZFuctoneCommandModuleImpl());
    }

    protected abstract Builder builder();

    protected static abstract class Builder {
        protected abstract PermissionRegistry buildPermissionRegistry();

        protected abstract PlatformPlayerAdapter buildPlayerAdapter();
        protected abstract PlatformServerAdapter buildServerAdapter();

        protected abstract MiniPlaceholdersModule buildMiniPlaceholdersModule();
        protected abstract PlaceholderAPIModule buildPlaceholderAPIModule();

        protected abstract CommandRegistry buildCommandRegistry();
    }
}
