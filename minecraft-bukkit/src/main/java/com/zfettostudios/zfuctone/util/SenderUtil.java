package com.zfettostudios.zfuctone.util;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.bukkit.integration.PlaceholderAPIIntegration;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SenderUtil {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static void sendMessage(CommandSender sender, String... message) {
        if (sender instanceof ConsoleCommandSender) BukkitZFuctone.getInstance().console().sendMessage(PlaceholderAPIIntegration.setPlaceholder(message));
        else if (sender instanceof Player player) player.sendMessage(mm.deserialize(PlaceholderAPIIntegration.setPlaceholder(player, message)));
    }
}
