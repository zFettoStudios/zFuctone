package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface CustomPlaceholderModule extends Module<CustomPlaceholderModule> {
    @NonNull PlaceholderConfig config();

    PermissionConfig.@NonNull Placeholder permission();
}