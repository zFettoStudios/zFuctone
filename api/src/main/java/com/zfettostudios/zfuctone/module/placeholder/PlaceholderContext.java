package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.BuilderProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface PlaceholderContext {
    static PlaceholderContext build() {
        return BuilderProvider.buildPlaceholderContext();
    }

    static PlaceholderContext build(@NonNull String rawtext) {
        return BuilderProvider.buildPlaceholderContextFromRawText(rawtext);
    }

    <T> T set(@NonNull String key, @NonNull T value);

    @Nullable <T> T get(@NonNull String key, @NonNull Class<T> target);
    @Nullable Object get(@NonNull String key);

    @Nullable <T> T remove(@NonNull String key, @NonNull Class<T> target);
    @Nullable Object remove(@NonNull String key);

    PlatformPlayer setSender(@NonNull PlatformPlayer sender);
    @Nullable PlatformPlayer getSender();

    String setRawText(@NonNull String rawText);
    @Nullable String getRawText();

    @NonNull Map<String, Object> getRawContext();
}