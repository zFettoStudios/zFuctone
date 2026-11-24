package com.zfettostudios.zfuctone.platform.registry.cloud.mapper;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.util.LazyInstance;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class PaperSenderMapper implements SenderMapper<CommandSourceStack, PlatformPlayer> {
    private final LazyInstance<PlatformPlayerService> playerService = LazyInstance.build(() -> ZFuctoneProvider.get().playerService());
    private final ZLogger logger = ZFuctoneProvider.get().logger();

    @Override
    public @NonNull PlatformPlayer map(@NonNull CommandSourceStack base) {
        if (!(base.getSender() instanceof Player player)) return logger;

        PlatformPlayer platformPlayer = playerService.get().getPlatformPlayer(player.getUniqueId());
        if (platformPlayer == null) return logger;

        return platformPlayer;
    }

    @Override
    public @NonNull CommandSourceStack reverse(@NonNull PlatformPlayer mapped) {
        if (mapped.isConsole()) return MinecraftServer.getServer().createCommandSourceStack();

        return ((CraftPlayer) mapped.getRawPlayer(Player.class)).getHandle().createCommandSourceStack();
    }
}
