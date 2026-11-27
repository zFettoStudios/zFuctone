package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.listener.BukkitListener;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.BukkitPlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.platform.adapter.BukkitPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.BukkitServerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.BukkitCommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.BukkitPermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.resolver.library.LibraryResolver;
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
    public @NonNull <L> L getLoader(Class<L> loaderClass) {
        if (loaderClass.isInstance(loader)) return loaderClass.cast(loader);

        throw new InvalidTypeException(loaderClass);
    }

    @Override
    public @NonNull Object getLoader() {
        return loader;
    }

    @Override
    protected Path getDataPath() {
        return loader.getDataFolder().toPath();
    }

    @Override
    protected void registerListeners() {
        listeners.clear();

        register(new BukkitListener());
    }

    private void register(Listener listener) {
        listeners.add(listener);
        pm.registerEvents(listener, loader);
    }

    @Override
    protected void unregisterListeners() {
        for (Listener listener : listeners) HandlerList.unregisterAll(listener);
    }

    @Override
    protected void registerTaskScheduler() {
        Bukkit.getScheduler().runTaskTimer(loader, taskScheduler::onTick, 0L, 1L);
    }

    @Override
    protected Builder builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends Builder {
        @Override
        protected PermissionRegistry buildPermissionRegistry() {
            return new BukkitPermissionRegistry();
        }

        @Override
        protected PlatformPlayerAdapter buildPlayerAdapter() {
            return new BukkitPlayerAdapter();
        }

        @Override
        protected PlatformServerAdapter buildServerAdapter() {
            return new BukkitServerAdapter();
        }

        @Override
        protected PlaceholderAPIModule buildPlaceholderAPIModule() {
            return new BukkitPlaceholderAPIModule();
        }

        @Override
        protected CommandRegistry buildCommandRegistry() {
            return new BukkitCommandRegistry();
        }
    }
}
