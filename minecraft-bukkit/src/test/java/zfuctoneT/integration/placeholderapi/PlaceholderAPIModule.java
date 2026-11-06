package zfuctoneT.integration.placeholderapi;

import zfuctoneT.ZFuctone;
import zfuctoneT.model.config.IntegrationConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIModule {
    private final static IntegrationConfig.PlaceholderAPI config = ZFuctone.get(IntegrationConfig.class).placeholderapi();
    private static final PlaceholderAPIIntegration integration = new PlaceholderAPIIntegration();

    public static void enable() {
        integration.hook();
    }

    public static void disable() {
        integration.unhook();
    }

    public static String setPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, setNestedPlaceholders(player, message));
    }

    public static String setPlaceholders(OfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player, setNestedPlaceholders(player, message));
    }

    private static String setNestedPlaceholders(Player player, String message) {
        if (!config.processNestedPlaceholders()) return message;
        return PlaceholderAPI.setBracketPlaceholders(player, message);
    }

    private static String setNestedPlaceholders(OfflinePlayer player, String message) {
        if (!config.processNestedPlaceholders()) return message;
        return PlaceholderAPI.setBracketPlaceholders(player, message);
    }
}
