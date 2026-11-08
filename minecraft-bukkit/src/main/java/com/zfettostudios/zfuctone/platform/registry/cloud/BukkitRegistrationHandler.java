package com.zfettostudios.zfuctone.platform.registry.cloud;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.Command;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.setting.ManagerSetting;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.Suggestions;
import org.incendo.cloud.util.StringUtils;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BukkitRegistrationHandler extends PlatformRegistrationHandler<CommandSender> {
    Map<String, BukkitCommand> registeredCommands = new ConcurrentHashMap<>();
    Plugin plugin;

    CommandMap commandMap = Bukkit.getCommandMap();
    Map<String, org.bukkit.command.Command> knownCommands = commandMap.getKnownCommands();

    @Override
    public boolean registerCommand(@NonNull Command<PlatformPlayer> command) {
        CommandComponent<PlatformPlayer> component = command.rootComponent();
        String label = component.name();
        if (registeredCommands.containsKey(label)) return false;

        List<String> aliases = new ArrayList<>(component.alternativeAliases());
        BukkitCommand bukkitCommand = new BukkitCommand(label, aliases, command);
        registeredCommands.put(label, bukkitCommand);

        runSync(() -> {
            if (manager.settings().get(ManagerSetting.OVERRIDE_EXISTING_COMMANDS)) {
                knownCommands.remove(label);
                aliases.forEach(knownCommands::remove);
            }

            commandMap.register(label, plugin.getName().toLowerCase(), bukkitCommand);
        });
        return true;
    }

    @Override
    public void unregisterRootCommand(@NonNull CommandComponent<PlatformPlayer> rootCommand) {
        BukkitCommand bukkitCommand = registeredCommands.remove(rootCommand.name());
        if (bukkitCommand == null) return;

        runSync(() -> {
            knownCommands.values().removeIf(command -> command == bukkitCommand);
            bukkitCommand.unregister(commandMap);

            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
        });
    }

    public boolean isRegisteredCommand(String label) {
        return knownCommands.get(label) instanceof BukkitCommand;
    }

    private void runSync(Runnable runnable) {
        runnable.run();
    }

    private class BukkitCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {
        private final Command<PlatformPlayer> command;

        private BukkitCommand(String label, List<String> aliases, Command<PlatformPlayer> command) {
            super(label, command.commandDescription().description().textDescription(), "", aliases);

            this.command = command;
        }

        @Override
        public boolean execute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String @NonNull [] args) {
            PlatformPlayer player = manager.senderMapper().map(sender);

            manager.commandExecutor().executeCommand(player, getInput(args));
            return true;
        }

        @Override
        public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, @NonNull String @NonNull [] args) {
            PlatformPlayer player = manager.senderMapper().map(sender);

            Suggestions<PlatformPlayer, ? extends Suggestion> suggestions = manager.suggestionFactory().suggestImmediately(player, getInput(args));
            return suggestions.list().stream()
                .map(suggestion -> StringUtils.trimBeforeLastSpace(suggestion.suggestion(), suggestions.commandInput()))
                .filter(Objects::nonNull)
                .toList();
        }

        @Override
        public boolean testPermissionSilent(@NonNull CommandSender target) {
            if (!isRegistered()) return false;

            PlatformPlayer player = manager.senderMapper().map(target);
            return manager.testPermission(player, command.commandPermission()).allowed();
        }

        @Override
        public @NonNull Plugin getPlugin() {
            return plugin;
        }

        private String getInput(String[] args) {
            return args.length == 0 ? getName() : getName() + " " + String.join(" ", args);
        }
    }

}
