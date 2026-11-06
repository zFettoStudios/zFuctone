package com.zfettostudios.zfuctone.platform.registry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.platform.mapper.BukkitZPlayerMapper;
import com.zfettostudios.zfuctone.platform.registry.cloud.BukkitRegistrationHandler;
import com.zfettostudios.zfuctone.platform.registry.cloud.PlatformCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.jspecify.annotations.NonNull;

@Getter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class BukkitCommandRegistry extends CommandRegistryImpl {
    @NonNull PlatformCommandManager<CommandSender> platformCommandManager;

    public BukkitCommandRegistry(JavaPlugin plugin) {
        platformCommandManager = new PlatformCommandManager<>(
            ExecutionCoordinator.asyncCoordinator(),
            new BukkitRegistrationHandler(plugin),
            new BukkitZPlayerMapper()
        );

        super();
    }
}
