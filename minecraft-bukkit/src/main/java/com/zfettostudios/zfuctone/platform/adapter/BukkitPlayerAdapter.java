package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.util.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitPlayerAdapter implements PlatformPlayerAdapter {
    private static final Map<Integer, List<String>> VERSIONS = new ConcurrentHashMap<>();

    static {
        // 26.x
        VERSIONS.put(777, List.of("26.3"));
        VERSIONS.put(776, List.of("26.2"));
        VERSIONS.put(775, List.of("26.1", "26.1.1", "26.1.2"));

        // 1.21.x
        VERSIONS.put(774, List.of("1.21.11"));
        VERSIONS.put(773, List.of("1.21.9", "1.21.10"));
        VERSIONS.put(772, List.of("1.21.7", "1.21.8"));
        VERSIONS.put(771, List.of("1.21.6"));
        VERSIONS.put(770, List.of("1.21.5"));
        VERSIONS.put(769, List.of("1.21.4"));
        VERSIONS.put(768, List.of("1.21.2", "1.21.3"));
        VERSIONS.put(767, List.of("1.21", "1.21.1"));

        // 1.20.x
        VERSIONS.put(766, List.of("1.20.5", "1.20.6"));
        VERSIONS.put(765, List.of("1.20.3", "1.20.4"));
        VERSIONS.put(764, List.of("1.20.2"));
        VERSIONS.put(763, List.of("1.20", "1.20.1"));

        // 1.19.x
        VERSIONS.put(762, List.of("1.19.4"));
        VERSIONS.put(761, List.of("1.19.3"));
        VERSIONS.put(760, List.of("1.19.1", "1.19.2"));
        VERSIONS.put(759, List.of("1.19"));

        // 1.18.x
        VERSIONS.put(758, List.of("1.18.2"));
        VERSIONS.put(757, List.of("1.18", "1.18.1"));

        // 1.17.x
        VERSIONS.put(756, List.of("1.17.1"));
        VERSIONS.put(755, List.of("1.17"));

        // 1.16.x
        VERSIONS.put(754, List.of("1.16.4", "1.16.5"));
        VERSIONS.put(753, List.of("1.16.3"));
        VERSIONS.put(751, List.of("1.16.2"));
        VERSIONS.put(749, List.of("1.16.1"));
        VERSIONS.put(736, List.of("1.16"));

        // 1.15.x
        VERSIONS.put(735, List.of("1.15.2"));
        VERSIONS.put(715, List.of("1.15.1"));
        VERSIONS.put(578, List.of("1.15"));

        // 1.14.x
        VERSIONS.put(575, List.of("1.14.4"));
        VERSIONS.put(574, List.of("1.14.3"));
        VERSIONS.put(573, List.of("1.14.2"));
        VERSIONS.put(498, List.of("1.14.1"));
        VERSIONS.put(477, List.of("1.14"));

        // 1.13.x
        VERSIONS.put(404, List.of("1.13.2"));
        VERSIONS.put(401, List.of("1.13.1"));
        VERSIONS.put(393, List.of("1.13"));

        // 1.12.x
        VERSIONS.put(340, List.of("1.12.2"));
        VERSIONS.put(338, List.of("1.12.1"));
        VERSIONS.put(335, List.of("1.12"));

        // 1.11.x
        VERSIONS.put(316, List.of("1.11.2"));
        VERSIONS.put(315, List.of("1.11"));

        // 1.10.x
        VERSIONS.put(210, List.of("1.10", "1.10.1", "1.10.2"));

        // 1.9.x
        VERSIONS.put(110, List.of("1.9.4"));
        VERSIONS.put(109, List.of("1.9.2"));
        VERSIONS.put(108, List.of("1.9.1"));
        VERSIONS.put(107, List.of("1.9"));

        // 1.8.x
        VERSIONS.put(47, List.of("1.8", "1.8.8", "1.8.9"));

        // 1.7.x
        VERSIONS.put(5, List.of("1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10"));
        VERSIONS.put(4, List.of("1.7.2", "1.7.3", "1.7.4", "1.7.5"));
    }


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
    public int getProtocolVersion(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer != null) return platformPlayer.getProtocolVersion();

        return -1;
    }

    @Override
    public int getProtocolVersion(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.getProtocolVersion();

        return -1;
    }

    @Override
    public @Nullable UUID getUniqueIdFromNickname(@NonNull String nickname) {
        Player player = Bukkit.getPlayerExact(nickname);
        return player != null ? player.getUniqueId() : null;
    }

    @Override
    public @NonNull String getVersionFromProtocolVersion(int protocol) {
        return getVersionFromProtocolVersion("<version>", "<version> - <version:last>", protocol);
    }

    private String parseVersion(@NonNull String formatOne, @NonNull String format, List<String> versions) {
        String targetFormat = (versions != null && versions.size() == 1) ? formatOne : format;

        if (versions == null || versions.isEmpty()) return removeVersionTags(targetFormat);

        StringBuilder sb = new StringBuilder(targetFormat.length());
        int cursor = 0;
        int nextTag;

        while ((nextTag = targetFormat.indexOf("<version", cursor)) != -1) {
            sb.append(targetFormat, cursor, nextTag);

            int closeTag = targetFormat.indexOf(">", nextTag);
            if (closeTag == -1) break;

            String tagContent = targetFormat.substring(nextTag + 1, closeTag).trim();

            cursor = closeTag + 1;

            if (tagContent.equals("version")) sb.append(versions.getFirst());
            else if (tagContent.startsWith("version:")) {
                String arg = tagContent.substring("version:".length()).trim();

                if (arg.equalsIgnoreCase("last")) sb.append(versions.getLast());
                else {
                    try {
                        int index = Integer.parseInt(arg);
                        if (index >= 0 && index < versions.size()) sb.append(versions.get(index));

                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            else sb.append(targetFormat, nextTag, closeTag + 1);
        }

        return sb.append(targetFormat, cursor, targetFormat.length()).toString();
    }


    private static String removeVersionTags(String text) {
        return text.replaceAll("<version(?::(?:last|\\d+))?>", "");
    }

    @Override
    public @NonNull String getVersionFromProtocolVersion(@NonNull String formatOne, @NonNull String format, int protocol) {
        return parseVersion(formatOne, format, VERSIONS.get(protocol));
    }


    @Override
    public @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull UUID uuid, @NonNull Component message) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer != null) platformPlayer.sendMessage(message);

        return this;
    }

    @Override
    public @NonNull PlatformPlayerAdapter dispatchMessage(@NonNull Object platformPlayer, @NonNull Component message) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) player.sendMessage(message);

        return this;
    }

    @Override
    public @NonNull PlatformPlayerAdapter executeCommand(@NonNull UUID uuid, @NonNull String command) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer != null) Bukkit.dispatchCommand(platformPlayer, command);

        return this;
    }

    @Override
    public @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) Bukkit.dispatchCommand(player, command);

        return this;
    }

    @Override
    public @NonNull String getNickname(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player.getName();

        return "";
    }

    @Override
    public @NonNull String getNickname(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.getName();

        return "";
    }

    @Override
    public @Nullable UUID getUniqueId(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.getUniqueId();

        return null;
    }

    @Override
    public int getPing(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.getPing();

        return -1;
    }

    @Override
    public int getPing(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer == null) return -1;

        return platformPlayer.getPing();
    }

    @Override
    public @NonNull String getLocale(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer == null) return "";

        return StringUtil.localeToString(platformPlayer.locale());
    }

    @Override
    public @NonNull String getLocale(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return StringUtil.localeToString(player.locale());

        return "";
    }

    @Override
    public @Nullable String getIp(@NonNull UUID uuid) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer == null) return "";

        InetSocketAddress address = platformPlayer.getAddress();
        if (address == null) return "";

        return address.getHostName();
    }

    @Override
    public @Nullable String getIp(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player == null) return "";

        InetSocketAddress address = player.getAddress();
        if (address != null) return address.getHostName();

        return "";
    }

    @Override
    public boolean isOperator(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player.isOp();

        return Bukkit.getOfflinePlayer(uuid).isOp();
    }

    @Override
    public boolean isOperator(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.isOp();

        OfflinePlayer offlinePlayer = getOfflinePlayerFrom(platformPlayer);
        if (offlinePlayer != null) return offlinePlayer.isOp();

        return false;
    }

    @Override
    public boolean isSneaking(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        return player.isSneaking();
    }

    @Override
    public boolean isSneaking(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.isSneaking();

        return false;
    }

    @Override
    public boolean isDead(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        return player.isDead();
    }

    @Override
    public boolean isDead(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.isDead();

        return false;
    }

    @Override
    public boolean isOnline(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;

        return player.isOnline();
    }

    @Override
    public boolean isOnline(@NonNull Object platformPlayer) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) return player.isOnline();

        return false;
    }

    @Override
    public PlatformPlayerAdapter kick(@NonNull UUID uuid, @NonNull Component reason) {
        Player platformPlayer = Bukkit.getPlayer(uuid);
        if (platformPlayer != null) platformPlayer.kick(reason);

        return this;
    }

    @Override
    public PlatformPlayerAdapter kick(@NonNull Object platformPlayer, @NonNull Component reason) {
        Player player = getPlayerFrom(platformPlayer);
        if (player != null) player.kick(reason);

        return this;
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
