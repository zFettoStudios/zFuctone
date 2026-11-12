package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.StringUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BukkitPlaceholderAPIIntegration extends PlaceholderExpansion implements IntegrationHook<BukkitPlaceholderAPIIntegration> {
    private IntegrationConfig.PlaceholderAPI config() {
        return ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
    }

    @Override
    public @NotNull String getIdentifier() {
        return config().identifier();
    }

    @Override
    public @NotNull String getAuthor() {
        return BuildConstant.PROJECT_NAME;
    }

    @Override
    public @NotNull String getVersion() {
        return BuildConstant.PROJECT_VERSION;
    }

    @Override
    public @NonNull List<String> getPlaceholders() {
        return List.of(
            createPlaceholder("project_authors"),
            createPlaceholder("project_description"),
            createPlaceholder("project_website"),
            createPlaceholder("project_version"),
            createPlaceholder("project_reload"),
            createPlaceholder("player_locale"),
            createPlaceholder("player_version"),
            createPlaceholder("player_edition"),
            createPlaceholder("player_display_version"),
            createPlaceholder("player_display_edition"),
            createPlaceholder("server_type"),
            createPlaceholder("server_version")
        );
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public BukkitPlaceholderAPIIntegration hook() {
        register();
        return this;
    }

    @Override
    public BukkitPlaceholderAPIIntegration unhook() {
        unregister();
        return this;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return onRequest(player, params);
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        Player player = null;
        if (offlinePlayer.isOnline()) player = offlinePlayer.getPlayer();

        if (config().processNestedPlaceholders()) {
            if (player != null) params = PlaceholderAPI.setBracketPlaceholders(player, params);
            else params = PlaceholderAPI.setBracketPlaceholders(offlinePlayer, params);
        }

        PermissionConfig.Placeholder config = ZFuctoneAPI.get().fileFacade().permission().placeholder();

        if (player != null) {
            PlatformPlayer platformPlayer = PlatformPlayer.builder()
                .platformPlayer(player)
                .uuid(player.getUniqueId())
                .build();

            switch (params) {
                case "player_locale" -> {
                    if (!player.hasPermission(config.playerLocale().use().name())) return null;
                    return StringUtil.localeToString(player.locale());
                }
                case "player_version" -> {
                    if (!player.hasPermission(config.playerVersion().use().name())) return null;
                    return platformPlayer.getVersion();
                }
                case "player_edition" -> {
                    if (!player.hasPermission(config.playerEdition().use().name())) return null;
                    PlayerEdition edition = platformPlayer.getEdition();
                    return edition.name();
                }
                case "player_display_version" -> {
                    if (!player.hasPermission(config.playerDisplayVersion().use().name())) return null;
                    return platformPlayer.getDisplayVersion();
                }
                case "player_display_edition" -> {
                    if (!player.hasPermission(config.playerDisplayEdition().use().name())) return null;
                    return platformPlayer.getDisplayEdition();
                }
            }
        }

        return switch (params) {
            case "project_authors" -> {
                if (!(player != null && player.hasPermission(config.projectAuthors().use().name()))) yield null;
                yield BuildConstant.PROJECT_AUTHORS;
            }
            case "project_description" -> {
                if (!(player != null && player.hasPermission(config.projectDescription().use().name()))) yield null;
                yield BuildConstant.PROJECT_DESCRIPTION;
            }
            case "project_website" -> {
                if (!(player != null && player.hasPermission(config.projectWebsite().use().name()))) yield null;
                yield BuildConstant.PROJECT_WEBSITE;
            }
            case "project_version" -> {
                if (!(player != null && player.hasPermission(config.projectVersion().use().name()))) yield null;
                yield BuildConstant.PROJECT_VERSION;
            }
            case "project_reload" -> {
                if (!(player != null && player.hasPermission(config.projectReload().use().name()))) yield null;
                try {
                    ZFuctoneAPI.get().reload();
                } catch (ReloadException e) {
                    ZFuctoneAPI.get().logger().warn("Failed to reload ZFuctone \"{}\"", e.getMessage());
                }
                yield "completed";
            }
            case "server_type" -> ZFuctoneAPI.get().getPlatformType().name();
            case "server_version" -> Bukkit.getServer().getMinecraftVersion();
            default -> null;
        };
    }

    private String createPlaceholder(String name) {
        return "%" + config().identifier() + "_" + name + "%";
    }
}
