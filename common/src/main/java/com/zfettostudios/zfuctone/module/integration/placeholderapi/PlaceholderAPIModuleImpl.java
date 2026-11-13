package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.module.integration.placeholdersapi.PlaceholderAPIModule;

public abstract class PlaceholderAPIModuleImpl extends IntegrationModule implements PlaceholderAPIModule {
    protected IntegrationConfig.PlaceholderAPI config() {
        return ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
    }

    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public String getName() {
        return "PlaceholderAPI";
    }
}
