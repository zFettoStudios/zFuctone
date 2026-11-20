package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.listener.BukkitPlayerJoinListener;
import com.zfettostudios.zfuctone.listener.BukkitPlayerQuitListener;
import com.zfettostudios.zfuctone.listener.PaperPlayerJoinListener;
import com.zfettostudios.zfuctone.listener.PaperPlayerQuitListener;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.PaperMiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.BukkitPlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.platform.adapter.BukkitPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PaperServerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.resolver.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BukkitZFuctone extends ZFuctoneImpl {
    private final BukkitLoader loader;
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

    public BukkitZFuctone(BukkitLoader loader, ReflectionResolver reflectionResolver,
                          PlatformType platformType, LibraryResolver libraryResolver) {
        this.loader = loader;
        super(reflectionResolver, platformType, libraryResolver);
    }

    @Override
    public @NonNull <L> L getLoader(Class<L> loader) {
        if (loader.isInstance(loader)) return loader.cast(loader);

        throw new InvalidTypeException(loader);
    }

    @Override
    public @NonNull Object getLoader() {
        return loader;
    }

    @Override
    protected void addedPlatformModules() {
        moduleController.register(PlaceholderAPIModule.class, new BukkitPlaceholderAPIModule());
        moduleController.register(MiniPlaceholdersModule.class, new PaperMiniPlaceholdersModule());
    }

    @Override
    protected Path getDataPath() {
        return loader.getDataFolder().toPath();
    }

    @Override
    protected PlatformPlayerAdapter createPlayerAdapter() {
        return new BukkitPlayerAdapter();
    }

    @Override
    protected PlatformServerAdapter createServerAdapter() {
        return new PaperServerAdapter(loader);
    }

    @Override
    protected void registerListeners() {
        listeners.clear();

        register(createPlayerJoinListener());
        register(createPlayerQuitListener());
    }

    private void register(Listener listener) {
        listeners.add(listener);
        pm.registerEvents(listener, loader);
    }

    private Listener createPlayerJoinListener() {
        if (platformType == PlatformType.BUKKIT) return new BukkitPlayerJoinListener();
        return new PaperPlayerJoinListener();
    }

    private Listener createPlayerQuitListener() {
        if (platformType == PlatformType.BUKKIT) return new BukkitPlayerQuitListener();
        return new PaperPlayerQuitListener();
    }

    @Override
    protected void unregisterListeners() {
        for (Listener listener : listeners) HandlerList.unregisterAll(listener);
    }

    @Override
    protected void registerTaskScheduler() {
        Bukkit.getScheduler().runTaskTimer(loader, taskScheduler::onTick, 0L, 1L);
    }
}
