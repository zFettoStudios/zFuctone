package com.zfettostudios.zfuctone.config.model;

import com.zfettostudios.zfuctone.config.ConfigFile;
import com.zfettostudios.zfuctone.config.model.setting.PermissionSetting;
import lombok.Builder;
import lombok.With;
import org.bukkit.permissions.PermissionDefault;

@With
@Builder(toBuilder = true)
@ConfigFile(name = "permission.yml")
public record Permission(
    ZFuctone zfuctone
) {
    @With
    @Builder(toBuilder = true)
    public record ZFuctone(
        String name,
        PermissionDefault type,

        Reload reload
    ) implements PermissionSetting {
        @With
        @Builder(toBuilder = true)
        public record Reload(
            String name,
            PermissionDefault type
        ) implements PermissionSetting {
        }
    }
}
