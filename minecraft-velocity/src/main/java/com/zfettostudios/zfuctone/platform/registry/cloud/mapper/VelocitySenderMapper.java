package com.zfettostudios.zfuctone.platform.registry.cloud.mapper;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import net.kyori.adventure.identity.Identity;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public class VelocitySenderMapper implements SenderMapper<CommandSource, PlatformPlayer> {
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    private final ProxyServer server = ZFuctoneProvider.get().getLoader(VelocityLoader.class).getServer();

    @Override
    public @NonNull PlatformPlayer map(@NonNull CommandSource base) {
        Optional<UUID> uuid = base.get(Identity.UUID);
        if (uuid.isEmpty()) return ZFuctoneProvider.get().logger();

        PlatformPlayer player = playerService.getPlatformPlayer(uuid.get());
        if (player == null) return ZFuctoneProvider.get().logger();

        return player;
    }

    @Override
    public @NonNull CommandSource reverse(@NonNull PlatformPlayer mapped) {
        if (mapped.isConsole()) return server.getConsoleCommandSource();

        return mapped.getRawPlayer(Player.class);
    }
}
