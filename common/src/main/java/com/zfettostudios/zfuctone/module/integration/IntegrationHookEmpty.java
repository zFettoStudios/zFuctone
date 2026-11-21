package com.zfettostudios.zfuctone.module.integration;

public class IntegrationHookEmpty implements IntegrationHook<IntegrationHookEmpty> {
    @Override
    public IntegrationHookEmpty hook() {
        return this;
    }

    @Override
    public IntegrationHookEmpty unhook() {
        return this;
    }
}
