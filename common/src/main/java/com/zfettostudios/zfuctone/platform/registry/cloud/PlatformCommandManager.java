package com.zfettostudios.zfuctone.platform.registry.cloud;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.CloudCapability;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.SenderMapperHolder;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.jspecify.annotations.NonNull;

@Getter
@Accessors(fluent = true)
public class PlatformCommandManager<S> extends CommandManager<PlatformPlayer> implements SenderMapperHolder<S, PlatformPlayer> {
    SenderMapper<S, PlatformPlayer> senderMapper;

    public PlatformCommandManager(
        ExecutionCoordinator<PlatformPlayer> executionCoordinator,
        PlatformRegistrationHandler<S> registrationHandler,
        SenderMapper<S, PlatformPlayer> senderMapper
    ) {
        super(executionCoordinator, registrationHandler);

        this.senderMapper = senderMapper;

        registerCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION);
        registrationHandler.initialize(this);
    }

    @Override
    public boolean hasPermission(@NonNull PlatformPlayer sender, @NonNull String permission) {
        return false;
    }
}
