package com.zfettostudios.zfuctone.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.player.User;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayerImpl;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class PlatformPlayerFactoryImpl implements PlatformPlayerFactory {
    private final LazyInstance<PlatformPlayerAdapter> playerAdapter = LazyInstance.build(() -> ZFuctoneProvider.get().playerAdapter());
    private final PlayerManager playerManager = PacketEvents.getAPI().getPlayerManager();

    @Override
    public @Nullable PlatformPlayer build(@NonNull UUID uuid) {
        Object receivedPlayer = playerAdapter.get().getPlatformPlayer(uuid);
        if (receivedPlayer == null) return null;

        User receivedUser = playerManager.getUser(receivedPlayer);
        if (receivedUser == null) return null;

        return new PlatformPlayerImpl(uuid, receivedPlayer, receivedUser);
    }

    @Override
    public @Nullable PlatformPlayer build(@NonNull Object rawPlayer) {
        if (!playerAdapter.get().isPlatformPlayer(rawPlayer)) return null;

        UUID uuid = playerAdapter.get().getUUID(rawPlayer);
        if (uuid == null) return null;

        User receivedUser = playerManager.getUser(rawPlayer);
        if (receivedUser == null) return null;

        return new PlatformPlayerImpl(uuid, rawPlayer, receivedUser);
    }
}
