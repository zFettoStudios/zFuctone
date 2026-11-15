package com.zfettostudios.zfuctone.platform.adapter;

import org.jspecify.annotations.NonNull;

import java.util.List;

public interface PlatformServerAdapter {
    boolean isProjectExits(@NonNull String name);

    @NonNull List<Object> getOnlinePlatformPlayers();

    PlatformServerAdapter runTask(Runnable task);

    @NonNull String getVersion();

    int getPlayersOnline();
    int getPlayersMax();

    double[] getTPS();
}
