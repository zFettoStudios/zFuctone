package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContextImpl;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderModule;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
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
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final IntegrationConfig.PlaceholderAPI config = fileFacade.integration().placeholderapi();
    private final PlaceholderModule placeholderModule = ZFuctoneAPI.get().moduleController().placeholderModule();
    private String identifier;

    private PlatformPlayerService playerService() {
        return ZFuctoneAPI.get().playerService();
    }

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
        return placeholderModule.getPlaceholderAPIPlaceholders();
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
        PlaceholderContextImpl context = new PlaceholderContextImpl(params);

        if (player != null) {
            PlatformPlayer platformPlayer = playerService().getPlatformPlayer(player.getUniqueId());
            if (platformPlayer != null) context.setSender(platformPlayer);
        }

        for (Map.Entry<String, Function<PlaceholderContextImpl, String>> entry : placeholderModule.placeholders().entrySet())
            if (params.equals(entry.getKey())) return entry.getValue().apply(context);

        return null;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) return onPlaceholderRequest(null, params);
        return onPlaceholderRequest(offlinePlayer.getPlayer(), params);
    }
}
