package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

@UtilityClass
public class BukkitPlaceholderAPIModule {
    private static final IntegrationConfig.PlaceholderAPI config = ZFuctoneAPI.get(FileFacade.class).integration().placeholderapi();
    private static final BukkitPlaceholderAPIIntegration integration = new BukkitPlaceholderAPIIntegration();

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
        if (!config.processNestedPlaceholders()) return message;
        return PlaceholderAPI.setBracketPlaceholders(player, message);
    }
}
