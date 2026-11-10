package com.zfettostudios.zfuctone.platform.mapper;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

public class BukkitZPlayerMapper implements SenderMapper<CommandSender, PlatformPlayer> {
    @Override
    public @NonNull PlatformPlayer map(@NonNull CommandSender sender) {
        if (!(sender instanceof Player player))
            return PlatformPlayer.builder().build();

        return PlatformPlayer.builder()
            .platformPlayer(player)
            .uuid(player.getUniqueId())
            .build();
    }

    @Override
    public @NonNull CommandSender reverse(@NonNull PlatformPlayer player) {
        Player platformPlayer = ZFuctoneAPI.get().playerAdapter().getPlatformPlayer(player.getUniqueId(), Player.class);
        if (platformPlayer == null) return Bukkit.getConsoleSender();

        return platformPlayer;
    }
}
