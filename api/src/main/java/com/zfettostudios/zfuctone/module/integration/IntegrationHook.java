package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface IntegrationHook<R> {
    static @NonNull IntegrationHook<?> empty() {
        return FactoryProvider.modelFactory().emptyIntegrationHook();
    }

    R hook();
    R unhook();
}
