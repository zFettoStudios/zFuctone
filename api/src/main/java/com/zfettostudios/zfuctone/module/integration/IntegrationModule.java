package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.Module;

public interface IntegrationModule<T> extends Module<T> {
    boolean isPluginExits();

    String getName();
    boolean isEnable();
}
