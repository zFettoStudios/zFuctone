package com.zfettostudios.zfuctone.util;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;

@UtilityClass
public class PlaceholderHandlerUtil {
    public boolean cannotHandle(PlatformPlayer sender,
                                PlaceholderConfig.Standard.PlaceholderSetting config,
                                PermissionSetting permission) {
        if (!config.enable() || sender == null) return true;

        return !sender.hasPermission(permission.name());
    }
}
