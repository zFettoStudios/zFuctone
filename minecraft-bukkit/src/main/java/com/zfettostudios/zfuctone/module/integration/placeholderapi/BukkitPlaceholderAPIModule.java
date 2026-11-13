package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.module.integration.placeholdersapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BukkitPlaceholderAPIModule extends PlaceholderAPIModuleImpl {
    private final BukkitPlaceholderAPIIntegration integration = new BukkitPlaceholderAPIIntegration();

    @Override
    public PlaceholderAPIModule enable() {
        if (isEnable()) integration.hook();
        return this;
    }

    @Override
    public PlaceholderAPIModule disable() {
        if (isPluginExits()) integration.unhook();
        return this;
    }

    @Override
    public @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input) {
        if (!isEnable()) return input;
        if (sender == null || !(sender.getRawPlayer() instanceof Player player))
            return setPlaceholders(input);

        return setPlaceholders(player, input);
    }

    private String setPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, setNestedPlaceholders(player, message));
    }

    private String setNestedPlaceholders(Player player, String message) {
        if (!config().processNestedPlaceholders()) return message;
        return PlaceholderAPI.setBracketPlaceholders(player, message);
    }

    private String setPlaceholders(String input) {
        return setPlaceholders((Player) null, input);
    }
}
