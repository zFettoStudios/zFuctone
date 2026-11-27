package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BukkitPlaceholderAPIModule extends PlaceholderAPIModuleImpl {
    private final IntegrationHook<?> integration;

    public BukkitPlaceholderAPIModule() {
        if (isEnable()) integration = new BukkitPlaceholderAPIIntegration();
        else integration = IntegrationHook.empty();
    }

    @Override
    public @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input) {
        if (!isEnable()) return input;
        if (sender == null || !(sender.getRawPlayer() instanceof Player player))
            return setPlaceholders(input);

        return setPlaceholders(player, input);
    }

    private String setPlaceholders(Player player, String input) {
        return ZFuctoneProvider.get().taskScheduler().runSync(() -> PlaceholderAPI.setPlaceholders(player, setNestedPlaceholders(player, input))).join();
    }

    private String setNestedPlaceholders(Player player, String input) {
        if (!config().processNestedPlaceholders()) return input;
        return PlaceholderAPI.setBracketPlaceholders(player, input);
    }

    private String setPlaceholders(String input) {
        return setPlaceholders((Player) null, input);
    }

    @Override
    protected void onEnable() {
        integration.hook();
    }

    @Override
    protected void onDisable() {
        if (isPluginExits()) integration.unhook();
    }
}
