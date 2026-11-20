package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;

public interface PlatformServerAdapter {
    boolean isProjectExits(@NonNull String name);

    PlatformServerAdapter runTask(@NonNull Runnable task);

    int getPlayersOnline();
    int getPlayersMax();

    double[] getTPS();

    boolean isPrimaryThread();

    PlatformServerAdapter registerPermission(@NonNull String permission, @NonNull PermissionType type);
    PlatformServerAdapter unregisterPermission(@NonNull String permission);
}
