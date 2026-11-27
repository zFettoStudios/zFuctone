package com.zfettostudios.zfuctone.platform.registry.cloud.mapper;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.util.LazyInstance;
import net.kyori.adventure.identity.Identity;
import org.incendo.cloud.SenderMapper;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public class VelocitySenderMapper implements SenderMapper<CommandSource, PlatformPlayer> {
    private final LazyInstance<PlatformPlayerService> playerService = LazyInstance.build(ZFuctone::playerService);
    private final LazyInstance<ProxyServer> server = LazyInstance.build(zfuctone -> zfuctone.getLoader(VelocityLoader.class).getServer());

    @Override
    public @NonNull PlatformPlayer map(@NonNull CommandSource base) {
        Optional<UUID> uuid = base.get(Identity.UUID);
        if (uuid.isEmpty()) return ZFuctoneProvider.get().logger();

        PlatformPlayer player = playerService.get().getPlatformPlayer(uuid.get());
        if (player == null) return ZFuctoneProvider.get().logger();

        return player;
    }

    @Override
    public @NonNull CommandSource reverse(@NonNull PlatformPlayer mapped) {
        if (mapped.isConsole()) return server.get().getConsoleCommandSource();

        return mapped.getRawPlayer(Player.class);
    }
}
