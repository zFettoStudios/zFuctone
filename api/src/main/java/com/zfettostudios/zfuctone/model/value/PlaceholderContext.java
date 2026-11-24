package com.zfettostudios.zfuctone.model.value;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface PlaceholderContext {
    static @NonNull PlaceholderContext build() {
        return FactoryProvider.modelFactory().buildPlaceholderContext();
    }
    static @NonNull PlaceholderContext build(@NonNull String rawtext) {
        return FactoryProvider.modelFactory().buildPlaceholderContext(rawtext);
    }

    @NonNull <T> T set(@NonNull String key, @NonNull T value);

    @Nullable <T> T get(@NonNull String key, @NonNull Class<T> target);

    @Nullable Object get(@NonNull String key);

    @Nullable <T> T remove(@NonNull String key, @NonNull Class<T> target);

    @Nullable Object remove(@NonNull String key);

    @NonNull PlatformPlayer setSender(@NonNull PlatformPlayer sender);

    @Nullable PlatformPlayer getSender();

    @NonNull String setRawText(@NonNull String rawText);

    @Nullable String getRawText();

    @NonNull Map<String, Object> getRawContext();
}