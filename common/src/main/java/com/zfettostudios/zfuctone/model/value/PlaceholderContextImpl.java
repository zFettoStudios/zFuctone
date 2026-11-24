package com.zfettostudios.zfuctone.model.value;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
public class PlaceholderContextImpl implements PlaceholderContext {
    private final Map<String, Object> rawContext = new ConcurrentHashMap<>();
    private PlatformPlayer sender;
    private String rawText;

    public PlaceholderContextImpl(String rawText) {
        setRawText(rawText);
    }

    public <T> @NonNull T set(@NonNull String key, @NonNull T value) {
        rawContext.put(key, value);
        return value;
    }

    public @Nullable <T> T get(@NonNull String key, @NonNull Class<T> target) {
        Object value = rawContext.get(key);
        if (target.isInstance(value)) return target.cast(value);

        return null;
    }

    @Override
    public @Nullable Object get(@NonNull String key) {
        return rawContext.get(key);
    }

    public @Nullable <T> T remove(@NonNull String key, @NonNull Class<T> target) {
        Object value = rawContext.remove(key);
        if (target.isInstance(value)) return target.cast(value);

        return null;
    }

    @Override
    public @Nullable Object remove(@NonNull String key) {
        return rawContext.remove(key);
    }

    public @NonNull PlatformPlayer setSender(@NonNull PlatformPlayer sender) {
        return this.sender = sender;
    }

    public @Nullable PlatformPlayer getSender() {
        return sender;
    }

    public @NonNull String setRawText(@NonNull String rawText) {
        return this.rawText = rawText;
    }

    public @Nullable String getRawText() {
        return rawText;
    }

    public static PlaceholderContext build(@NonNull String rawtext) {
        return new PlaceholderContextImpl(rawtext);
    }
}