package com.zfettostudios.zfuctone.platform.registry.cloud;

import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import org.incendo.cloud.internal.CommandRegistrationHandler;

public abstract class PlatformRegistrationHandler<S> implements CommandRegistrationHandler<ZPlayer> {
    @NonFinal protected PlatformCommandManager<S> manager;

    public void initialize(PlatformCommandManager<S> manager) {
        this.manager = manager;
    }
}
