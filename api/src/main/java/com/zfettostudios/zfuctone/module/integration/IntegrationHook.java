package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.provider.BuilderProvider;
import org.jspecify.annotations.NonNull;

public interface IntegrationHook<R> {
    R hook();
    R unhook();

    static @NonNull IntegrationHook<?> empty() {
        return BuilderProvider.getEmptyIntegrationHook();
    }
}
