package com.zfettostudios.zfuctone.platform.registry;

import lombok.Getter;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class PlaceholderRegistryImpl implements PlaceholderRegistry {
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();

    @Getter
    private final Map<String, Function<PlaceholderContext, String>> placeholders = new ConcurrentHashMap<>();
    @Getter
    private final List<String> placeholderAPIPlaceholders = new CopyOnWriteArrayList<>();
    private final PlaceholderConfig config = fileFacade.placeholder();
    private final IntegrationConfig.PlaceholderAPI placeholderAPIConfig = fileFacade.integration().placeholderapi();

    @Override
    public PlaceholderRegistry register(@NonNull PlaceholderSetting setting, @NonNull PermissionSetting permission,
                                        @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler) {
        if (!config.enable() || !setting.enable()) return this;

        createPlaceholderAPIPlaceholder(setting.name());

        placeholders.put(setting.name(), context -> {
            PlatformPlayer player = context.getSender();
            if (player == null || player.hasPermission(permission.name())) return null;

            return handler.apply(context);
        });

        return this;
    }

    @Override
    public PlaceholderRegistry register(@NonNull PlaceholderSetting setting,
                                        @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler) {
        if (!config.enable() || !setting.enable()) return this;

        createPlaceholderAPIPlaceholder(setting.name());

        placeholders.put(setting.name(), handler);

        return this;
    }

    @Override
    public PlaceholderRegistry unregister(@NonNull String name) {
        placeholderAPIPlaceholders.remove(name);
        placeholders.remove(name);

        return this;
    }

    @Override
    public PlaceholderRegistry unregisterAll() {
        placeholderAPIPlaceholders.clear();
        placeholders.clear();

        return this;
    }

    private void createPlaceholderAPIPlaceholder(String name) {
        if (!config.enable() || !placeholderAPIConfig.enable()) return;

        placeholderAPIPlaceholders.add("%" + placeholderAPIConfig.identifier() + "_" + name + "%");
    }
}
