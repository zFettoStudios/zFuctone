package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.setting.ManagerSetting;

import java.util.function.Function;

public abstract class CommandRegistryImpl implements CommandRegistry {
    public CommandRegistryImpl() {
        platformCommandManager().settings()
            .set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true)
            .set(ManagerSetting.OVERRIDE_EXISTING_COMMANDS, true);
    }

    @Override
    public CommandRegistry register(Function<CommandManager<ZPlayer>, Command.Builder<ZPlayer>> builder) {
        Command<ZPlayer> command = builder.apply(platformCommandManager()).build();

        String commandName = command.rootComponent().name();

        boolean isCloudCommand = platformCommandManager().commands().stream()
            .anyMatch(zCommand -> zCommand.rootComponent().name().equals(commandName));
        if (isCloudCommand) return this;

        unregister(commandName);
        platformCommandManager().command(command);

        return this;
    }

    @Override
    public CommandRegistry unregister(String name) {
        platformCommandManager().deleteRootCommand(name);
        return this;
    }

    protected abstract CommandManager<ZPlayer> platformCommandManager();
}
