package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface CommandModuleController extends Module {
    @Nullable <T extends CommandModule<T>> T getModule(@NonNull String id, @NonNull Class<T> module);
    <T> T addModule(@NonNull String id, @NonNull CommandModule<T> module);
    @Nullable CommandModule<?> removeModule(@NonNull String id);
}