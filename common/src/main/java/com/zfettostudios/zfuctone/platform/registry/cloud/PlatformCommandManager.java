package com.zfettostudios.zfuctone.platform.registry.cloud;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import org.incendo.cloud.CloudCapability;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.SenderMapperHolder;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.jspecify.annotations.NonNull;

@Getter
@Accessors(fluent = true)
public class PlatformCommandManager<S> extends CommandManager<ZPlayer> implements SenderMapperHolder<S, ZPlayer> {
    SenderMapper<S, ZPlayer> senderMapper;

    public PlatformCommandManager(
        ExecutionCoordinator<ZPlayer> executionCoordinator,
        PlatformRegistrationHandler<S> registrationHandler,
        SenderMapper<S, ZPlayer> senderMapper
    ) {
        super(executionCoordinator, registrationHandler);

        this.senderMapper = senderMapper;

        registerCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION);
        registrationHandler.initialize(this);
    }

    @Override
    public boolean hasPermission(@NonNull ZPlayer sender, @NonNull String permission) {
        return false;
    }
}
