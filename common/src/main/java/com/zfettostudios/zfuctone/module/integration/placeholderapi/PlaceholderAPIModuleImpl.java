package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import org.jspecify.annotations.NonNull;

public abstract class PlaceholderAPIModuleImpl extends IntegrationModuleImpl<PlaceholderAPIModule> implements PlaceholderAPIModule {
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public @NonNull String getName() {
        return "PlaceholderAPI";
    }

    @Override
    public IntegrationConfig.@NonNull PlaceholderAPI config() {
        return ZFuctoneProvider.get().fileFacade().integration().placeholderapi();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().placeholderapi();
    }
}
