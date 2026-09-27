package com.zfettostudios.zfuctone.bukkit.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.CommandConfig;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.config.model.PermissionConfig;
import com.zfettostudios.zfuctone.util.SenderUtil;
import com.zfettostudios.zfuctone.util.StringUtil;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
public class SpawnCommand extends Command {
    private static final BukkitZFuctone zfuctone = BukkitZFuctone.getInstance();

    private CommandConfig.Spawn commandConfig;
    private PermissionConfig.Command.Spawn permissionConfig;

    public SpawnCommand(CommandConfig.Spawn commandConfig, PermissionConfig.Command.Spawn permissionConfig) {
        List<String> aliases = new ArrayList<>(commandConfig.aliases());
        aliases.removeFirst();

        super(commandConfig.aliases().getFirst(), "", "", aliases);

        this.commandConfig = commandConfig;
        this.permissionConfig = permissionConfig;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        Localization localization = zfuctone.console().getLocalization();
        if (sender instanceof Player player) localization = zfuctone.localizationManager().get(StringUtil.localeToString(player.locale()));

        if (!sender.hasPermission(permissionConfig.name())) {
            SenderUtil.sendMessage(sender, localization.command().spawn().notPermission());
            return false;
        }

        if (args.length != 0 && commandConfig.other().aliases().stream().anyMatch(alias -> alias.equals(args[0]))) {
        }
        else {
            SenderUtil.sendMessage(sender, localization.command().spawn().invalidArguments());
            return false;
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        List<String> tab = new ArrayList<>();

        return tab;
    }
}