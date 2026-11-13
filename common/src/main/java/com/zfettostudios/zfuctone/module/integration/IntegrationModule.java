package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.ZFuctoneAPI;

public abstract class IntegrationModule {
    protected boolean isEnable(boolean enable) {
        return enable && isPluginExits();
    }

    public boolean isPluginExits() {
        return ZFuctoneAPI.get().serverAdapter().isProjectExits(getName());
    }

    public abstract String getName();
    public abstract boolean isEnable();
}
