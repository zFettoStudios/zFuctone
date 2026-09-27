package com.zfettostudios.zfuctone.bukkit.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.config.model.CommandConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import java.util.Map;

public class CommandManager {
    private static final String PROJECT_PREFIX = BuildConfig.PROJECT_NAME.toLowerCase() + ":";

    public void init() {
        CommandConfig commandConfig = BukkitZFuctone.getInstance().configManager().staticConfig().command();

        update(new ZFuctoneCommand(), commandConfig.zfuctone().enable());
    }

    public void update(Command command, boolean enable) {
        if (enable) register(command);
        else unregister(command);
    }

    public void register(Command command) {
        if (command.isRegistered()) unregister(command);
        command.register(Bukkit.getCommandMap());
    }

    public void unregister(Command command) {
        command.unregister(Bukkit.getCommandMap());

        Map<String, Command> knownCommands = Bukkit.getCommandMap().getKnownCommands();

        knownCommands.remove(command.getName());
        knownCommands.remove(PROJECT_PREFIX + command.getName());

        for (String alias : command.getAliases()) {
            knownCommands.remove(alias);
            knownCommands.remove(PROJECT_PREFIX + alias);
        }
    }

    public void unregister(String commandName) {
        Command command = Bukkit.getCommandMap().getCommand(commandName);
        if (command != null) unregister(command);
    }

    public void reload() {
        CommandConfig commandConfig = BukkitZFuctone.getInstance().configManager().staticConfig().command();

        update(new ZFuctoneCommand(), commandConfig.zfuctone().enable());
    }

    public void unregisterAll() {
        CommandConfig commandConfig = BukkitZFuctone.getInstance().configManager().staticConfig().command();

        unregister(commandConfig.zfuctone().aliases().getFirst());
    }
}
