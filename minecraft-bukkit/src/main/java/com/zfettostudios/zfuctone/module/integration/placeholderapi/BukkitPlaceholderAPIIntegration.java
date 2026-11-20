package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BukkitPlaceholderAPIIntegration extends PlaceholderExpansion implements IntegrationHook<BukkitPlaceholderAPIIntegration> {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneAPI.get().placeholderRegistry();
    private final PlatformPlayerService playerService = ZFuctoneAPI.get().playerService();

    private final IntegrationConfig.PlaceholderAPI config = ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
    private String identifier;

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
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
        return placeholderRegistry.getPlaceholderAPIPlaceholders();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public BukkitPlaceholderAPIIntegration hook() {
        identifier = config.identifier();

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
        params = params.toLowerCase();
        PlaceholderContext context = PlaceholderContext.build(params);

        if (player != null) {
            PlatformPlayer platformPlayer = playerService.getPlatformPlayer(player.getUniqueId());
            if (platformPlayer != null) context.setSender(platformPlayer);
        }

        for (Map.Entry<String, Function<PlaceholderContext, String>> entry : placeholderRegistry.getPlaceholders().entrySet())
            if (params.equals(entry.getKey())) return entry.getValue().apply(context);

        return null;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) return onPlaceholderRequest(null, params);
        return onPlaceholderRequest(offlinePlayer.getPlayer(), params);
    }
}
