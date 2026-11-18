package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderValueSetting;
import com.zfettostudios.zfuctone.module.ReloadModule;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PlaceholderModule extends ReloadModule<PlaceholderModule> {
    Map<String, Function<PlaceholderContext, String>> placeholders();
    List<String> getPlaceholderAPIPlaceholders();
    PlaceholderModule addPlaceholder(@NonNull PlaceholderSetting config,
                                     @NonNull PermissionSetting permission,
                                     @NonNull Function<PlaceholderContext, String> handler);
    PlaceholderModule addPlaceholder(@NonNull PlaceholderSetting config,
                                     @NonNull Function<PlaceholderContext, String> handler);
    boolean isEnable();
}
