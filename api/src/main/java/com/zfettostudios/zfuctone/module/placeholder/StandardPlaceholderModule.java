package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import org.jspecify.annotations.NonNull;

public interface StandardPlaceholderModule {
    PlaceholderConfig.@NonNull Standard config();

    PermissionConfig.Placeholder.@NonNull Standard permission();
}
