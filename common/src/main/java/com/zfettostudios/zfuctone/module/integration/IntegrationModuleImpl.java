package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;

public abstract class IntegrationModuleImpl<T> extends ExtendedModule<T> {
    protected boolean isEnable(boolean enable) {
        return enable && isPluginExits();
    }

    public boolean isPluginExits() {
        return ZFuctoneProvider.get().serverAdapter().isProjectExists(getName());
    }

    public abstract String getName();
}
