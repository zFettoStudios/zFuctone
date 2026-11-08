package com.zfettostudios.zfuctone.platform.registry.cloud;

import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.internal.CommandRegistrationHandler;

public abstract class PlatformRegistrationHandler<S> implements CommandRegistrationHandler<PlatformPlayer> {
    @NonFinal protected PlatformCommandManager<S> manager;

    public void initialize(PlatformCommandManager<S> manager) {
        this.manager = manager;
    }
}
