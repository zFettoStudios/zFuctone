package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.ReloadModule;

public interface IntegrationModule<T> extends ReloadModule<T> {
    boolean isPluginExits();

    String getName();
    boolean isEnable();

    T get();
}
