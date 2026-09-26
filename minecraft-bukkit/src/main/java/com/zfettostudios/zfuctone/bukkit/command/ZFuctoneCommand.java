package com.zfettostudios.zfuctone.bukkit.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.config.model.Permission;
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
public class ZFuctoneCommand extends Command {
    private static final BukkitZFuctone zfuctone = BukkitZFuctone.getInstance();

    private com.zfettostudios.zfuctone.config.model.Command.ZFuctone commandConfig;
    private Permission.Command.ZFuctone permissionConfig;

    public ZFuctoneCommand(com.zfettostudios.zfuctone.config.model.Command.ZFuctone commandConfig, Permission.Command.ZFuctone permissionConfig) {
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
            SenderUtil.sendMessage(sender, localization.command().zfuctone().notPermission());
            return false;
        }

        if (args.length == 0) {
            SenderUtil.sendMessage(sender, localization.command().zfuctone().invalidArguments());
            return false;
        }

        if (commandConfig.reload().aliases().stream().anyMatch(alias -> alias.equals(args[0]))) {
            zfuctone.reload();
            SenderUtil.sendMessage(sender, localization.command().zfuctone().reload());
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> tab = new ArrayList<>();

        if (!sender.hasPermission(permissionConfig.name())) return tab;

        switch (args.length) {
            case 0 -> {
                if (sender.hasPermission(permissionConfig.reload().name())) tab.addAll(commandConfig.reload().aliases());
            }
        }

        return tab;
    }
}
