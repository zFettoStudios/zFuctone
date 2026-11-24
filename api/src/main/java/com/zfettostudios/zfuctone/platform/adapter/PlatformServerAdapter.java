package com.zfettostudios.zfuctone.platform.adapter;

import org.jspecify.annotations.NonNull;

public interface PlatformServerAdapter {
    boolean isProjectExists(@NonNull String name);

    @NonNull PlatformServerAdapter runTask(@NonNull Runnable task);

    int getPlayersOnline();

    int getPlayersMax();

    double[] getTPS();

    boolean isPrimaryThread();
}