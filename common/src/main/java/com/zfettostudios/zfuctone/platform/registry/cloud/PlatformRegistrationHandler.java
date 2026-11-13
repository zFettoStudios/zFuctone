package com.zfettostudios.zfuctone.platform.registry.cloud;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.internal.CommandRegistrationHandler;

public abstract class PlatformRegistrationHandler<S> implements CommandRegistrationHandler<PlatformPlayer> {
    protected PlatformCommandManager<S> manager;

    public void initialize(PlatformCommandManager<S> manager) {
        this.manager = manager;
    }
}
