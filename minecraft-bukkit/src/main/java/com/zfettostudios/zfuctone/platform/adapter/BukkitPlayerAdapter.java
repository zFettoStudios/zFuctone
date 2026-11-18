package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.Gamemode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.UUID;

public class BukkitPlayerAdapter extends PlatformPlayerAdapterImpl {
    @Override
    public @Nullable <T> T getPlatformPlayer(@NonNull UUID uuid, @NonNull Class<T> target) {
        return target.cast(Bukkit.getPlayer(uuid));
    }

    @Override
    public @Nullable Object getPlatformPlayer(@NonNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public boolean isPlatformPlayer(Object platformPlayer) {
        return platformPlayer instanceof Player;
    }

    @Override
    @Deprecated(forRemoval = true)
    public int getProtocolVersion(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer == null) return -1;

        return -1;
    }

    @Override
    @Deprecated(forRemoval = true)
    public int getProtocolVersion(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return -1;

        return -1;
    }

    @Override
    public @Nullable UUID getUUIDFromNickname(@NonNull String nickname) {
        Player player = Bukkit.getPlayerExact(nickname);
        return player != null ? player.getUniqueId() : null;
    }

    @Override
    public @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull Object platformPlayer, @NonNull Component message) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) player.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));

        return this;
    }

    @Override
    public @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) Bukkit.dispatchCommand(player, command);

        return this;
    }

    @Override
    public @NonNull String getNickname(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        return player.getName();
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

        return player.getPing();
    }

    @Override
    public @NonNull String getLocale(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer == null) return "";

        return platformPlayer.getLocale();
    }

    @Override
    public @NonNull String getLocale(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        return player.getLocale();
    }

    @Override
    public @Nullable String getIp(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        InetSocketAddress address = player.getAddress();
        if (address == null) return "";

        return address.getHostName();
    }

    @Override
    public boolean isOperator(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.isOp();

        OfflinePlayer offlinePlayer = getOfflinePlayerFrom(platformPlayer);
        if (offlinePlayer == null) return false;

        return offlinePlayer.isOp();
    }

    @Override
    public boolean isSneaking(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return false;

        return player.isSneaking();
    }

    @Override
    public boolean isDead(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return false;

        return player.isDead();
    }

    @Override
    public boolean isOnline(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return false;

        return player.isOnline();
    }

    @Override
    public boolean hasPermission(@NonNull String permission, @NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return false;

        return player.hasPermission(permission);
    }

    @Override
    public PlatformPlayerAdapter kick(@NonNull Object platformPlayer, @NonNull Component reason) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) player.kickPlayer(LegacyComponentSerializer.legacySection().serialize(reason));

        return this;
    }

    @Override
    public @NonNull Gamemode getGamemode(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return Gamemode.CREATIVE;

        return switch (player.getGameMode()) {
            case CREATIVE -> Gamemode.CREATIVE;
            case SURVIVAL -> Gamemode.SURVIVAL;
            case ADVENTURE -> Gamemode.ADVENTURE;
            case SPECTATOR -> Gamemode.SPECTATOR;
        };
    }

    @Override
    public @NonNull String getWorldName(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        return player.getWorld().getName();
    }

    @Override
    public @NonNull String getWorldEnvironment(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        return player.getWorld().getEnvironment().name();
    }

    private @Nullable Player getPlayerFrom(Object object) {
        if (object instanceof Player player) return player;
        return null;
    }

    private @Nullable OfflinePlayer getOfflinePlayerFrom(Object object) {
        if (object instanceof OfflinePlayer player) return player;
        return null;
    }
}
