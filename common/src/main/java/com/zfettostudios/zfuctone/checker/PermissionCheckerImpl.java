package com.zfettostudios.zfuctone.checker;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

public class PermissionCheckerImpl implements PermissionChecker {
    private LuckPermsModule luckPerms;

    private LuckPermsModule luckPerms() {
        if (luckPerms == null) luckPerms = ZFuctoneAPI.get().moduleController().getIntegration(LuckPermsModule.class);
        return luckPerms;
    }

    @Override
    public boolean check(@NonNull PlatformPlayer player, @NonNull String permission) {
        if (luckPerms().isEnable()) return luckPerms.hasPermission(player, permission);

        return player.hasPermission(permission);
    }
}