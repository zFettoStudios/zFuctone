package com.zfettostudios.zfuctone.module.integration;

public interface IntegrationHook<R> {
    R hook();
    R unhook();
}
