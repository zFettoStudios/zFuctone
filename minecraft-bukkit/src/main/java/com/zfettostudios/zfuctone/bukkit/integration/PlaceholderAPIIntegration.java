package com.zfettostudios.zfuctone.bukkit.integration;

import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.util.StringUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderAPIIntegration {
    public static String setPlaceholder(Player player, String... text) {
        return PlaceholderAPI.setPlaceholders(player, StringUtil.join(text));
    }

    public static String setPlaceholder(OfflinePlayer player, String... text) {
        return PlaceholderAPI.setPlaceholders(player, StringUtil.join(text));
    }

    public static String setPlaceholder(String... text) {
        return PlaceholderAPI.setPlaceholders(null, StringUtil.join(text));
    }

    public static class Expansion extends PlaceholderExpansion {
        private static final String identifier = "zfuctone";

        @Override
        public @NotNull String getIdentifier() {
            return identifier;
        }

        @Override
        public @NotNull String getAuthor() {
            return BuildConfig.PROJECT_AUTHORS;
        }

        @Override
        public @NotNull String getVersion() {
            return BuildConfig.PROJECT_VERSION;
        }

        @Override
        public @NotNull List<String> getPlaceholders() {
            List<String> placeholders = new ArrayList<>();

            placeholders.add("");

            return placeholders;
        }

        @Override
        public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
            Player player = offlinePlayer.getPlayer();

            return onPlaceholderRequest(player, params);
        }

        @Override
        public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
            if (params.startsWith(Names.FUNCTION_NAME.value)) {
                String function = params.substring(Names.FUNCTION_NAME.value.length());
            }
            else if (params.equals(Names.PLAYER_LOCALE.value)) return StringUtil.localeToString(player.locale());

            return null;
        }

        private enum Names {
            FUNCTION_NAME("_function_"),

            SCRIPT_NAME("_script:"),

            PLAYER_LOCALE("_player_locale");

            private final String value;

            Names(String value) {
                this.value = value;
            }
        }
    }
}
