package com.zfettostudios.zfuctone.platform.registry.cloud.mapper;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

public class BukkitSenderMapper implements SenderMapper<CommandSender, PlatformPlayer> {
    private final LazyInstance<PlatformPlayerService> playerService = LazyInstance.build((ZFuctone::playerService));
    private final LazyInstance<ZLogger> logger = LazyInstance.build(ZFuctone::logger);

    @Override
    public @NonNull PlatformPlayer map(@NonNull CommandSender base) {
        if (!(base instanceof Player player)) return logger.get();

        PlatformPlayer platformPlayer = playerService.get().getPlatformPlayer(player.getUniqueId());
        if (platformPlayer == null) return logger.get();

        return platformPlayer;
    }

    @Override
    public @NonNull CommandSender reverse(@NonNull PlatformPlayer mapped) {
        if (mapped.isPlayer()) return mapped.getRawPlayer(Player.class);

        return Bukkit.getConsoleSender();
    }
}