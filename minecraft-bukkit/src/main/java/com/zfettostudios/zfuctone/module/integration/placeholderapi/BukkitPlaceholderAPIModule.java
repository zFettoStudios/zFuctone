package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

@UtilityClass
public class BukkitPlaceholderAPIModule {
    private static final BukkitPlaceholderAPIIntegration integration = new BukkitPlaceholderAPIIntegration();

    private static IntegrationConfig.PlaceholderAPI config() {
        return ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
    }

    public static void enable() {
        integration.hook();
    }

    public static void disable() {
        integration.unhook();
    }

    public static String setPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, setNestedPlaceholders(player, message));
    }

    private static String setNestedPlaceholders(Player player, String message) {
        if (!config().processNestedPlaceholders()) return message;
        return PlaceholderAPI.setBracketPlaceholders(player, message);
    }
}
