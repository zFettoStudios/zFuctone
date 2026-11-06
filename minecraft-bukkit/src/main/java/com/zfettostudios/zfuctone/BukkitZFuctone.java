package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.module.integration.BukkitIntegration;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.adapter.BukkitEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.BukkitPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.BukkitServerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.BukkitCommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public class BukkitZFuctone extends ZFuctoneImpl {
    JavaPlugin javaPlugin;
    PlatformPlayerAdapter playerAdapter;
    PlatformServerAdapter serverAdapter;
    PlatformEntityAdapter entityAdapter;
    Integration integration;
    CommandRegistry commandRegistry;

    public BukkitZFuctone(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;

        super();

        serverAdapter = new BukkitServerAdapter(javaPlugin);
        playerAdapter = new BukkitPlayerAdapter();
        entityAdapter = new BukkitEntityAdapter();
        integration = new BukkitIntegration();
        commandRegistry = new BukkitCommandRegistry(javaPlugin);
    }

    @Override
    protected void platformEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                scheduler.onTick();
            }
        }.runTaskTimer(javaPlugin, 0L, 1L);

        integration.enable();
    }

    @Override
    protected void platformDisable() {
        integration.disable();
    }

    @Override
    protected ZFuctone platformReload() {
        platformDisable();
        platformEnable();
        return this;
    }

    @Override
    public @NonNull <L> L getLoader(Class<L> loader) {
        if (loader.isInstance(javaPlugin)) return loader.cast(javaPlugin);

        throw new InvalidTypeException(loader);
    }

    @Override
    protected <T> T platformGet(Class<T> type) {
        if (type.isInstance(playerAdapter)) return type.cast(playerAdapter);
        else if (type.isInstance(serverAdapter)) return type.cast(serverAdapter);
        else if (type.isInstance(entityAdapter)) return type.cast(entityAdapter);
        else if (type.isInstance(integration)) return type.cast(integration);
        else if (type.isInstance(commandRegistry)) return type.cast(commandRegistry);

        throw new InvalidTypeException(type);
    }

    @Override
    protected Path getDataPath() {
        return javaPlugin.getDataPath();
    }

    @Override
    protected ComponentLogger getComponentLogger() {
        return javaPlugin.getComponentLogger();
    }
}
