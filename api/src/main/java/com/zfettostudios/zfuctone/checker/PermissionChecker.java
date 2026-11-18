package com.zfettostudios.zfuctone.checker;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

public interface PermissionChecker {
    boolean check(@NonNull PlatformPlayer player, @NonNull String permission);
}
