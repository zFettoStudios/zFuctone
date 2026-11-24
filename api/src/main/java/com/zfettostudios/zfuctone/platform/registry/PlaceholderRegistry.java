package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PlaceholderRegistry {
    @NonNull PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name, @NonNull String permission,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    @NonNull PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    @NonNull PlaceholderRegistry unregister(@NonNull String name);

    @NonNull PlaceholderRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String name);

    @NonNull PlaceholderRegistry unregisterAll(@NonNull Class<?> moduleClass);

    @NonNull PlaceholderRegistry unregisterAll();

    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull String name);

    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull Class<?> moduleClass, @NonNull String name);

    @Nullable Map<String, Function<@NonNull PlaceholderContext, @Nullable String>> getPlaceholders(@NonNull Class<?> moduleClass);

    @NonNull Map<Class<?>, Map<String, Function<@NonNull PlaceholderContext, @Nullable String>>> getPlaceholders();

    @NonNull List<String> getPlaceholderAPIPlaceholders();
}