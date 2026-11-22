package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public abstract class StandardPlaceholderModule<T> extends SimpleModule<T> {
    @Override
    public PlaceholderConfig.@NonNull Standard config() {
        return ZFuctoneProvider.get().fileFacade().placeholder().standard();
    }

    @Override
    public PermissionConfig.Placeholder.@NonNull Standard permission() {
        return ZFuctoneProvider.get().fileFacade().permission().placeholder().standard();
    }
}
