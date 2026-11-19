package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.ReloadModule;
import org.jspecify.annotations.NonNull;

public interface IntegrationModule<T> extends ReloadModule<T> {
    boolean isPluginExits();

    @NonNull String getName();
    boolean isEnable();

    T get();
}
