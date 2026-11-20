package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PlaceholderRegistry {
    PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name, @NonNull String permission,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    PlaceholderRegistry register(@NonNull Class<?> moduleClass, @NonNull String name,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    PlaceholderRegistry unregister(@NonNull String name);
    PlaceholderRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String name);

    PlaceholderRegistry unregisterAll(@NonNull Class<?> moduleClass);
    PlaceholderRegistry unregisterAll();

    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull String name);
    @Nullable Function<@NonNull PlaceholderContext, @Nullable String> get(@NonNull Class<?> moduleClass, @NonNull String name);

    boolean contains(@NonNull Class<?> moduleClass, @NonNull String name);
    boolean contains(@NonNull String name);

    @Nullable Map<String, Function<@NonNull PlaceholderContext, @Nullable String>> getPlaceholders(@NonNull Class<?> moduleClass);
    @NonNull Map<Class<?>, Map<String, Function<@NonNull PlaceholderContext, @Nullable String>>> getPlaceholders();
    @NonNull List<String> getPlaceholderAPIPlaceholders();
}