package com.zfettostudios.zfuctone;

import lombok.Getter;
import lombok.experimental.Accessors;

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
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class BukkitZFuctone extends ZFuctoneImpl {
    JavaPlugin javaPlugin;

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
    protected CommandRegistry createCommandRegistry() {
        return new BukkitCommandRegistry(javaPlugin);
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
}
