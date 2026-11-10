package com.zfettostudios.zfuctone.module.message;

import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface MessageModuleController extends Module<MessageModuleController> {
    @Nullable <T extends MessageModule<T>> T getModule(@NonNull String id, @NonNull Class<T> module);
    <T> T addModule(@NonNull String id, @NonNull MessageModule<T> module);
    @Nullable MessageModule<?> removeModule(@NonNull String id);
}
