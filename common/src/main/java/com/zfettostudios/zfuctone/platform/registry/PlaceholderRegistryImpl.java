package com.zfettostudios.zfuctone.platform.registry;

import lombok.Getter;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderRegistryImpl implements PlaceholderRegistry {
    @Getter
    private final Map<String, Function<PlaceholderContext, String>> placeholders = new LinkedHashMap<>();

    @Override
    public PlaceholderRegistry register(@NonNull PlaceholderSetting setting, @NonNull PermissionSetting permission,
                                        @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler) {
        return this;
    }

    @Override
    public PlaceholderRegistry unregister(@NonNull String name) {
        return this;
    }
}
