package com.zfettostudios.zfuctone.platform.adapter;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class VelocityPlayerAdapter implements PlatformPlayerAdapter {
    private final LazyInstance<ProxyServer> server = LazyInstance.build(zfuctone -> zfuctone.getLoader(VelocityLoader.class).getServer());

    @Override
    public @Nullable <T> T getPlatformPlayer(@NonNull UUID uuid, @NonNull Class<T> target) {
        return target.cast(server.get().getPlayer(uuid).orElse(null));
    }

    @Override
    public @Nullable Object getPlatformPlayer(@NonNull UUID uuid) {
        return server.get().getPlayer(uuid);
    }

    @Override
    public boolean isPlatformPlayer(Object platformPlayer) {
        return platformPlayer instanceof Player;
    }

    @Override
    public @Nullable UUID getUUIDFromNickname(@NonNull String nickname) {
        Player player = server.get().getPlayer(nickname).orElse(null);
        if (player == null) return null;

        return player.getUniqueId();
    }

    @Override
    public @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command) {
        return this;
    }

    @Override
    public @NonNull String getNickname(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        return player.getUsername();
    }

    @Override
    public @Nullable UUID getUUID(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return null;

        return player.getUniqueId();
    }

    @Override
    public int getPing(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return -1;

        return Math.toIntExact(player.getPing());
    }

    @Override
    public @NonNull String getLocale(@NonNull Object platformPlayer) {
        return "";
    }

    @Override
    public boolean isOperator(@NonNull Object platformPlayer) {
        return false;
    }

    @Override
    public boolean isSneaking(@NonNull Object platformPlayer) {
        return false;
    }

    @Override
    public boolean isDead(@NonNull Object platformPlayer) {
        return false;
    }

    @Override
    public boolean isOnline(@NonNull Object platformPlayer) {
        return false;
    }

    @Override
    public boolean hasPermission(@NonNull Object platformPlayer, @NonNull String permission) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return false;

        return player.hasPermission(permission);
    }

    @Override
    public @NonNull PlayerGameMode getGamemode(@NonNull Object platformPlayer) {
        return PlayerGameMode.UNKNOWN;
    }

    @Override
    public @NonNull String getWorldName(@NonNull Object platformPlayer) {
        return "";
    }

    @Override
    public @NonNull String getWorldEnvironment(@NonNull Object platformPlayer) {
        return "";
    }

    private @Nullable Player getPlayerFrom(Object object) {
        if (object instanceof Player player) return player;
        return null;
    }
}
