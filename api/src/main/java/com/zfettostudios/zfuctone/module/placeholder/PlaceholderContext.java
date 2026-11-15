package com.zfettostudios.zfuctone.module.placeholder;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class PlaceholderContext {
    private final Map<String, Object> rawContext = new LinkedHashMap<>();
    private PlatformPlayer sender;
    private String rawText;

    public PlaceholderContext(@NonNull String rawText) {
        setRawText(rawText);
    }

    public <T> T set(String key, T value) {
        rawContext.put(key, value);
        return value;
    }

    public @Nullable <T> T get(String key, Class<T> target) {
        Object value = rawContext.get(key);
        if (target.isInstance(value)) return target.cast(value);

        return null;
    }

    public @Nullable <T> T remove(String key, Class<T> target) {
        Object value = rawContext.remove(key);
        if (target.isInstance(value)) return target.cast(value);

        return null;
    }

    public PlatformPlayer setSender(PlatformPlayer sender) {
        return this.sender = sender;
    }

    public @Nullable PlatformPlayer getSender() {
        return sender;
    }

    public @Nullable PlatformPlayer removeSender() {
        if (sender == null) return null;

        PlatformPlayer cachedSender = sender.copy();

        sender = null;

        return cachedSender;
    }

    public String setRawText(String rawText) {
        return this.rawText = rawText;
    }

    public @Nullable String getRawText() {
        return rawText;
    }

    public @Nullable String removeRawText() {
        if (rawText == null) return null;

        String cachedRawText = rawText;

        rawText = null;

        return cachedRawText;
    }
}