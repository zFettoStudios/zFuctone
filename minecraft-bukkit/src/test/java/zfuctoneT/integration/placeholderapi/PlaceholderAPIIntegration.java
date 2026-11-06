package zfuctoneT.integration.placeholderapi;

import zfuctoneT.ZFuctone;
import com.zfettostudios.zfuctone.config.BuildConfig;
import zfuctoneT.integration.IntegrationHook;
import zfuctoneT.model.config.IntegrationConfig;
import zfuctoneT.util.PlayerUtil;
import zfuctoneT.util.text.StringUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PlaceholderAPIIntegration extends PlaceholderExpansion implements IntegrationHook<PlaceholderAPIIntegration> {
    private final static IntegrationConfig.PlaceholderAPI config = ZFuctone.get(IntegrationConfig.class).placeholderapi();

    @Override
    public @NotNull String getIdentifier() {
        return config.identifier();
    }

    @Override
    public @NotNull String getAuthor() {
        return BuildConfig.PROJECT_NAME;
    }

    @Override
    public @NotNull String getVersion() {
        return BuildConfig.PROJECT_VERSION;
    }

    @Override
    public @NonNull List<String> getPlaceholders() {
        return List.of(
            createPlaceholder("project_authors"),
            createPlaceholder("project_description"),
            createPlaceholder("project_website"),
            createPlaceholder("project_version"),
            createPlaceholder("player_locale"),
            createPlaceholder("player_version"),
            createPlaceholder("player_edition")
        );
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public PlaceholderAPIIntegration hook() {
        register();
        return this;
    }

    @Override
    public PlaceholderAPIIntegration unhook() {
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

        if (config.processNestedPlaceholders()) {
            if (player != null) params = PlaceholderAPI.setBracketPlaceholders(player, params);
            else params = PlaceholderAPI.setBracketPlaceholders(offlinePlayer, params);
        }

        if (player != null) switch (params) {
            case "player_locale" -> {
                return StringUtil.localeToString(player.locale());
            }
            case "player_version" -> {
                return PlayerUtil.getVersion(player);
            }
            case "player_edition" -> {
                return PlayerUtil.getEdition(player.getUniqueId()).name();
            }
        }

        return switch (params) {
            case "project_authors" -> BuildConfig.PROJECT_AUTHORS;
            case "project_description" -> BuildConfig.PROJECT_DESCRIPTION;
            case "project_website" -> BuildConfig.PROJECT_WEBSITE;
            case "project_version" -> BuildConfig.PROJECT_VERSION;
            default -> null;
        };
    }

    private String createPlaceholder(String name) {
        return "%" + config.identifier() + "_" + name + "%";
    }
}
