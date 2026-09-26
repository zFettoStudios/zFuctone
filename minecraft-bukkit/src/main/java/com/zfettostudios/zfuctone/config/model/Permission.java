package com.zfettostudios.zfuctone.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zfettostudios.zfuctone.config.FileName;
import com.zfettostudios.zfuctone.config.model.setting.CommandPermissionSetting;
import com.zfettostudios.zfuctone.config.model.setting.PermissionSetting;
import lombok.Builder;
import lombok.With;
import org.bukkit.permissions.PermissionDefault;

@With
@Builder(toBuilder = true)
@FileName("permission.yml")
public record Permission(
    Command command
) {
    @With
    @Builder(toBuilder = true)
    public record Command(
        ZFuctone zfuctone,
        Spawn spawn,
        SetSpawn setspawn
    ) {
        @With
        @Builder(toBuilder = true)
        public record ZFuctone(
            String name,
            PermissionDefault type,
            @JsonProperty("cooldown_bypass")
            PermissionSection cooldownBypass,
            PermissionSection sound,

            Reload reload
        ) implements CommandPermissionSetting {
            @With
            @Builder(toBuilder = true)
            public record Reload(
                String name,
                PermissionDefault type,
                @JsonProperty("cooldown_bypass")
                PermissionSection cooldownBypass,
                PermissionSection sound
            ) implements CommandPermissionSetting {
            }
        }

        @With
        @Builder(toBuilder = true)
        public record Spawn(
            String name,
            PermissionDefault type,
            @JsonProperty("cooldown_bypass")
            PermissionSection cooldownBypass,
            PermissionSection sound,

            Other other
        ) implements CommandPermissionSetting {
            @With
            @Builder(toBuilder = true)
            public record Other(
                String name,
                PermissionDefault type,
                @JsonProperty("cooldown_bypass")
                PermissionSection cooldownBypass,
                PermissionSection sound
            ) implements CommandPermissionSetting {
            }
        }

        @With
        @Builder(toBuilder = true)
        public record SetSpawn(
            String name,
            PermissionDefault type,
            @JsonProperty("cooldown_bypass")
            PermissionSection cooldownBypass,
            PermissionSection sound
        ) implements CommandPermissionSetting {
        }
    }

    @With
    @Builder(toBuilder = true)
    public record PermissionSection(
        String name,
        PermissionDefault type
    ) implements PermissionSetting {
    }
}
