package com.zfettostudios.zfuctone.platform.registry;

import lombok.Getter;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
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
    private final Map<Class<?>, Map<String, Function<PlaceholderContext, String>>> placeholders = new ConcurrentHashMap<>();
    @Getter
    private final List<String> placeholderAPIPlaceholders = new CopyOnWriteArrayList<>();
    private final PlaceholderConfig config = fileFacade.placeholder();
    private final IntegrationConfig.PlaceholderAPI placeholderAPIConfig = fileFacade.integration().placeholderapi();

    @Override
    public PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name, @NonNull String permission,
                                        @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler) {
        if (!config.enable()) return this;

        createPlaceholderAPIPlaceholder(name);

        Map<String, Function<PlaceholderContext, String>> modulePlaceholders = placeholders.get(moduleClass);
        if (modulePlaceholders == null) modulePlaceholders = new ConcurrentHashMap<>();

        modulePlaceholders.put(name, context -> {
            PlatformPlayer player = context.getSender();
            if (player == null || player.hasPermission(permission)) return null;

            return handler.apply(context);
        });

        placeholders.put(moduleClass, modulePlaceholders);

        return this;
    }

    @Override
    public PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name,
                                        @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler) {
        if (!config.enable()) return this;

        createPlaceholderAPIPlaceholder(name);

        Map<String, Function<PlaceholderContext, String>> modulePlaceholders = placeholders.get(moduleClass);
        if (modulePlaceholders == null) modulePlaceholders = new ConcurrentHashMap<>();

        modulePlaceholders.put(name, handler);
        
        placeholders.put(moduleClass, modulePlaceholders);

        return this;
    }

    @Override
    public PlaceholderRegistry unregister(@NonNull String name) {
        placeholderAPIPlaceholders.remove(name);
        placeholders.values().forEach(modulePlaceholders -> modulePlaceholders.remove(name));

        return this;
    }

    @Override
    public PlaceholderRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String name) {
        Map<String, Function<PlaceholderContext, String>> modulePlaceholders = placeholders.get(moduleClass);
        if (modulePlaceholders == null) return this;

        modulePlaceholders.remove(name);

        return this;
    }

    @Override
    public PlaceholderRegistry unregisterAll(@NonNull Class<?> moduleClass) {
        placeholders.remove(moduleClass);

        return this;
    }

    @Override
    public PlaceholderRegistry unregisterAll() {
        placeholderAPIPlaceholders.clear();
        placeholders.clear();

        return this;
    }

    @Override
    public @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull String name) {
        for (Map<String, Function<PlaceholderContext, String>> modulePlaceholders : placeholders.values()) {
            Function<PlaceholderContext, String> placeholder = modulePlaceholders.get(name);
            if (placeholder != null) return placeholder;
        }

        return null;
    }

    @Override
    public @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull Class<?> moduleClass, @NonNull String name) {
        Map<String, Function<PlaceholderContext, String>> modulePlaceholders = placeholders.get(moduleClass);
        if (modulePlaceholders == null) return null;

        return modulePlaceholders.get(name);
    }

    @Override
    public boolean contains(@NonNull Class<?> moduleClass, @NonNull String name) {
        Map<String, Function<PlaceholderContext, String>> modulePlaceholders = placeholders.get(moduleClass);
        if (modulePlaceholders == null) return false;

        return modulePlaceholders.containsKey(name);
    }

    @Override
    public boolean contains(@NonNull String name) {
        return placeholders.values().stream().anyMatch(modulePlaceholders -> modulePlaceholders.containsKey(name));
    }

    @Override
    public @Nullable Map<String, Function<@NonNull PlaceholderContext, @Nullable String>> getPlaceholders(@NonNull Class<?> moduleClass) {
        return placeholders.get(moduleClass);
    }

    private void createPlaceholderAPIPlaceholder(String name) {
        if (!config.enable() || !placeholderAPIConfig.enable()) return;

        placeholderAPIPlaceholders.add("%" + placeholderAPIConfig.identifier() + "_" + name + "%");
    }
}
