package com.zfettostudios.zfuctone.platform.registry;

import lombok.Getter;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.CloudCapability;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.setting.ManagerSetting;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

@Getter
public abstract class CommandRegistryImpl implements CommandRegistry {
    private final CommandManager<PlatformPlayer> commandManager = buildCommandManager();
    private final Map<Class<?>, Set<String>> commands = new ConcurrentHashMap<>();

    public CommandRegistryImpl() {
        commandManager.settings()
            .set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true)
            .set(ManagerSetting.OVERRIDE_EXISTING_COMMANDS, true);
    }

    @Override
    public @NonNull CommandRegistry register(@NonNull Class<?> moduleClass,
                                             @NonNull Function<@NonNull CommandManager<PlatformPlayer>,
                                        Command.@NonNull Builder<PlatformPlayer>> builder) {
        Command.Builder<PlatformPlayer> commandBuilder = builder.apply(commandManager);

        Permission permission = commandBuilder.commandPermission();
        String permissionName = permission.permissionString();

        Command<PlatformPlayer> command = commandBuilder.permission(player ->
            PermissionResult.of(player.hasPermission(permissionName), permission)).build();

        if (commandManager.commands().contains(command)) return this;

        String commandName = command.rootComponent().name();

        unregister(moduleClass, commandName);
        commandManager.command(command);

        Set<String> moduleCommands = commands.get(moduleClass);
        if (moduleCommands == null) moduleCommands = new CopyOnWriteArraySet<>();

        moduleCommands.add(commandName);

        commands.put(moduleClass, moduleCommands);

        return this;
    }

    @Override
    public @NonNull CommandRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String commandName) {
        if (!commandManager.hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) return this;

        Set<String> moduleCommands = commands.get(moduleClass);
        if (moduleCommands == null) return this;

        commandManager.deleteRootCommand(commandName);
        moduleCommands.remove(commandName);

        return this;
    }

    @Override
    public @NonNull CommandRegistry unregister(@NonNull String commandName) {
        if (!commandManager.hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) return this;

        commands.values().forEach(moduleCommands -> moduleCommands.remove(commandName));

        commandManager.deleteRootCommand(commandName);

        return this;
    }

    @Override
    public @NonNull CommandRegistry unregisterAll(@NonNull Class<?> moduleClass) {
        if (!commandManager.hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) return this;

        Set<String> moduleCommands = commands.get(moduleClass);
        if (moduleCommands == null) return this;

        moduleCommands.forEach(commandManager::deleteRootCommand);
        moduleCommands.clear();

        return this;
    }

    @Override
    public @NonNull CommandRegistry unregisterAll() {
        if (!commandManager.hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) return this;

        commands.values().forEach(moduleCommands -> moduleCommands.forEach(commandManager::deleteRootCommand));

        commands.clear();

        return this;
    }

    @Override
    public @Nullable Set<String> getCommands(@NonNull Class<?> moduleClass) {
        return commands.get(moduleClass);
    }

    protected abstract CommandManager<PlatformPlayer> buildCommandManager();
}