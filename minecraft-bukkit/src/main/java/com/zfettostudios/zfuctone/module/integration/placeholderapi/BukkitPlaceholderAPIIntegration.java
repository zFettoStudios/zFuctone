package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
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
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();

    private final IntegrationConfig.PlaceholderAPI config = ZFuctoneProvider.get().fileFacade().integration().placeholderapi();
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

        if (player == null) return null;

        PlatformPlayer platformPlayer = playerService.getPlatformPlayer(player.getUniqueId());
        if (platformPlayer == null) return null;

        context.setSender(platformPlayer);

        for (Map<String, Function<PlaceholderContext, String>> modulePlaceholders : placeholderRegistry.getPlaceholders().values()) {
            for (Map.Entry<String, Function<PlaceholderContext, String>> entry : modulePlaceholders.entrySet())
                if (params.equals(entry.getKey())) return entry.getValue().apply(context);
        }

        return null;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) return onPlaceholderRequest(null, params);
        return onPlaceholderRequest(offlinePlayer.getPlayer(), params);
    }
}
