package com.zfettostudios.zfuctone.module.integration;

import org.jspecify.annotations.NonNull;

public class IntegrationHookEmpty implements IntegrationHook<IntegrationHookEmpty> {
    @Override
    public @NonNull IntegrationHookEmpty hook() {
        return this;
    }

    @Override
    public @NonNull IntegrationHookEmpty unhook() {
        return this;
    }
}
