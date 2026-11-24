package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

public interface MessageDispatcher {
    @NonNull MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull GetMessage getMessage,
                               @NonNull PlatformPlayer... recipients);

    @NonNull MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull GetMessage getMessage);
}