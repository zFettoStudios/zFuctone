package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface IntegrationModule<T> extends Module<T> {
    boolean isPluginExits();

    @NonNull String getName();
}
