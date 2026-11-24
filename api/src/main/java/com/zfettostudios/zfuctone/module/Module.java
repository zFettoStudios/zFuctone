package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.model.config.setting.EnableSetting;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import org.jspecify.annotations.NonNull;

public interface Module<T> extends Reloadable<T> {
    @NonNull T get();

    boolean isEnable();
    boolean isDisable();

    @NonNull EnableSetting config();

    @NonNull PermissionSetting permission();

    @NonNull Class<? extends Module<T>> moduleClass();
}