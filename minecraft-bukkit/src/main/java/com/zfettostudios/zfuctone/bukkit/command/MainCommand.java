package com.zfettostudios.zfuctone.bukkit.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.config.model.Permission;
import com.zfettostudios.zfuctone.util.SenderUtil;
import com.zfettostudios.zfuctone.util.StringUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    private final Permission.ZFuctone permission;
    private final Localization consoleLocalization;

    public MainCommand(Permission.ZFuctone permission, Localization consoleLocalization) {
        this.permission = permission;
        this.consoleLocalization = consoleLocalization;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Localization localization = consoleLocalization;
        if (sender instanceof Player player) localization = BukkitZFuctone.getInstance().localizationManager().get(StringUtil.localeToString(player.locale()));

        if (!sender.hasPermission(permission.name())) {
            SenderUtil.sendMessage(sender, localization.command().zfuctone().notPermission());
            return false;
        }

        if (args.length == 0) {
            SenderUtil.sendMessage(sender, localization.command().zfuctone().invalidArguments());
            return false;
        }

        switch (args[0]) {
            case "reload" -> {
                BukkitZFuctone.getInstance().reload();
                SenderUtil.sendMessage(sender, localization.command().zfuctone().reload());
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> tab = new ArrayList<>();

        if (!sender.hasPermission(permission.name())) return tab;

        switch (args.length) {
            case 0 -> {
                if (sender.hasPermission(permission.reload().name())) tab.add("reload");
            }
        }

        return tab;
    }
}
