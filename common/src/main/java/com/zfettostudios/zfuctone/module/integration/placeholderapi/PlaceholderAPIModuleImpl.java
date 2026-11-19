package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import org.jspecify.annotations.NonNull;

public abstract class PlaceholderAPIModuleImpl extends IntegrationModuleImpl implements PlaceholderAPIModule {
    protected IntegrationConfig.PlaceholderAPI config() {
        return ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
    }

    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public @NonNull String getName() {
        return "PlaceholderAPI";
    }
}
