package com.zfettostudios.zfuctone.platform.mapper;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import com.zfettostudios.zfuctone.model.entity.ZPlayerImpl;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

public class BukkitZPlayerMapper implements SenderMapper<CommandSender, ZPlayer> {
    PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);

    @Override
    public @NonNull ZPlayer map(@NonNull CommandSender sender) {
        if (!(sender instanceof Player player))
            return ZPlayerImpl.builder()
                .isConsole(true)
                .isOnline(false)
                .ip(null)
                .uuid(null)
                .name(sender.getName())
                .type(sender.getName())
                .build();

        return ZPlayerImpl.builder()
            .isConsole(false)
            .isOnline(true)
            .ip(playerAdapter.getIp(player))
            .uuid(player.getUniqueId())
            .name(player.getName())
            .type(player.getType().name())
            .build();
    }

    @Override
    public @NonNull CommandSender reverse(@NonNull ZPlayer player) {
        if (player.isConsole()) return Bukkit.getConsoleSender();

        Player platformPlayer = playerAdapter.getPlatformPlayer(player.uuid(), Player.class);
        if (platformPlayer == null) return Bukkit.getConsoleSender();

        return platformPlayer;
    }
}
