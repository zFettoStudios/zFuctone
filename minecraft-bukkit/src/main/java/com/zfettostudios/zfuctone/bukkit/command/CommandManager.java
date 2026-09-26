package com.zfettostudios.zfuctone.bukkit.command;

import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.config.model.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    private static final CommandMap commandMap = Bukkit.getCommandMap();

    private final Map<String, Command> commands = new ConcurrentHashMap<>();

    public void init(com.zfettostudios.zfuctone.config.model.Command commandConfig, Permission permissionConfig) {
        register(new ZFuctoneCommand(commandConfig.zfuctone(), permissionConfig.command().zfuctone()));
    }

    public void register(Command command) {
        if (command.isRegistered()) unregister(command);

        command.register(commandMap);
        commands.put(command.getName(), command);
    }

    public void unregister(Command command) {
        command.unregister(commandMap);

        commandMap.getKnownCommands().remove(command.getName());
        commandMap.getKnownCommands().remove(BuildConfig.PROJECT_NAME.toLowerCase() + command.getName());

        command.getAliases().forEach(alias -> {
            commandMap.getKnownCommands().remove(alias);
            commandMap.getKnownCommands().remove(BuildConfig.PROJECT_NAME.toLowerCase() + alias);
        });

        commands.remove(command.getName());
    }

    public void reload() {
        new HashMap<>(commands).forEach((_, command) -> {
            unregister(command);
            register(command);
        });
    }
}
