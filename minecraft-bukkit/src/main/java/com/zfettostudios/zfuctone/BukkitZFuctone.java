package com.zfettostudios.zfuctone;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.listener.BukkitPlayerJoinListener;
import com.zfettostudios.zfuctone.listener.BukkitPlayerQuitListener;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.integration.BukkitIntegration;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.adapter.BukkitEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.BukkitPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.BukkitServerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Accessors(fluent = true)
public class BukkitZFuctone extends ZFuctoneImpl {
    @Getter private final JavaPlugin javaPlugin;
    private final PluginManager pm = Bukkit.getPluginManager();
    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

    public BukkitZFuctone(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;

        super();
    }

    @Override
    public @NonNull <L> L getLoader(Class<L> loader) {
        if (loader.isInstance(javaPlugin)) return loader.cast(javaPlugin);

        throw new InvalidTypeException(loader);
    }

    @Override
    public @NonNull Object getLoader() {
        return javaPlugin;
    }

    @Override
    protected PlatformType detectPlatformType() {
        if (reflectionResolver.hasClass("io.papermc.paper.threadedregions.RegionizedServer")) return PlatformType.FOLIA;
        if (reflectionResolver.hasClass("io.papermc.paper.ServerBuildInfo") ||
            reflectionResolver.hasClass("com.destroystokyo.paper.ParticleBuilder")) return PlatformType.PAPER;

        return PlatformType.BUKKIT;
    }

    @Override
    protected Path getDataPath() {
        return javaPlugin.getDataPath();
    }

    @Override
    protected ComponentLogger getComponentLogger() {
        return javaPlugin.getComponentLogger();
    }

    @Override
    protected Integration createIntegration() {
        return new BukkitIntegration();
    }

    @Override
    protected PlatformPlayerAdapter createPlayerAdapter() {
        return new BukkitPlayerAdapter();
    }

    @Override
    protected PlatformEntityAdapter createEntityAdapter() {
        return new BukkitEntityAdapter();
    }

    @Override
    protected PlatformServerAdapter createServerAdapter() {
        return new BukkitServerAdapter(javaPlugin);
    }

    @Override
    protected void registerListeners() {
        listeners.clear();

        register(new BukkitPlayerJoinListener());
        register(new BukkitPlayerQuitListener());
    }

    private void register(Listener listener) {
        listeners.add(listener);
        pm.registerEvents(listener, javaPlugin);
    }

    @Override
    protected void unregisterListeners() {
        for (Listener listener : listeners) HandlerList.unregisterAll(listener);
    }

}
