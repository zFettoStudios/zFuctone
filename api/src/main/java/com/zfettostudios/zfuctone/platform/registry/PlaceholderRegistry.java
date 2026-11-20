package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public interface PlaceholderRegistry {
    PlaceholderRegistry register(@NonNull PlaceholderSetting setting, @NonNull PermissionSetting permission,
                                 @NonNull Function<@NonNull PlaceholderContext, @Nullable String> handler);

    PlaceholderRegistry unregister(@NonNull String name);

    @NonNull Map<String, Function<PlaceholderContext, String>> getPlaceholders();
}
