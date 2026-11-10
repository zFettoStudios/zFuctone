package com.zfettostudios.zfuctone.module.message;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageModuleControllerImpl implements MessageModuleController {
    Map<String, MessageModule<?>> modules = new ConcurrentHashMap<>();

    @Override
    public MessageModuleController enable() {
        return this;
    }

    @Override
    public MessageModuleController disable() {
        return this;
    }

    @Override
    public @Nullable <T extends MessageModule<T>> T getModule(@NonNull String id, @NonNull Class<T> module) {
        MessageModule<?> receivedModule = modules.get(id);
        if (module.isInstance(receivedModule.get())) return module.cast(receivedModule.get());


        return null;
    }

    @Override
    public <T> T addModule(@NonNull String id, @NonNull MessageModule<T> module) {
        modules.put(id, module);
        return module.get();
    }

    @Override
    public @Nullable MessageModule<?> removeModule(@NonNull String id) {
        return modules.remove(id);
    }
}
