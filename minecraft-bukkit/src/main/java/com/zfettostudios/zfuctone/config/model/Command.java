package com.zfettostudios.zfuctone.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zfettostudios.zfuctone.config.FileName;
import com.zfettostudios.zfuctone.config.model.setting.CommandSetting;
import com.zfettostudios.zfuctone.config.model.setting.CooldownSetting;
import com.zfettostudios.zfuctone.config.model.setting.SoundSetting;
import com.zfettostudios.zjtime.TimeUnit;
import lombok.Builder;
import lombok.With;
import org.bukkit.SoundCategory;

import java.util.List;

@With
@Builder(toBuilder = true)
@FileName("command.yml")
public record Command(
    ZFuctone zfuctone,
    Spawn spawn,
    SetSpawn setspawn
) {
    @With
    @Builder(toBuilder = true)
    public record ZFuctone(
        Boolean enable,
        List<String> aliases,
        Cooldown cooldown,
        Sound sound,

        Reload reload
    ) implements CommandSetting {
        @With
        @Builder(toBuilder = true)
        public record Reload(
            Boolean enable,
            List<String> aliases,
            Cooldown cooldown,
            Sound sound
        ) implements CommandSetting {
        }
    }

    @With
    @Builder(toBuilder = true)
    public record Spawn(
        Boolean enable,
        List<String> aliases,
        Cooldown cooldown,
        Sound sound,

        Other other
    ) implements CommandSetting {
        @With
        @Builder(toBuilder = true)
        public record Other(
            Boolean enable,
            List<String> aliases,
            Cooldown cooldown,
            Sound sound
        ) implements CommandSetting {
        }
    }

    @With
    @Builder(toBuilder = true)
    public record SetSpawn(
        Boolean enable,
        List<String> aliases,
        Cooldown cooldown,
        Sound sound
    ) implements CommandSetting {
    }

    @With
    @Builder(toBuilder = true)
    public record Cooldown(
        Boolean enable,
        Long duration,
        @JsonProperty("time_unit")
        TimeUnit timeUnit
    ) implements CooldownSetting {
    }

    @With
    @Builder(toBuilder = true)
    public record Sound(
        Boolean enable,
        Float volume,
        Float pitch,
        SoundCategory category,
        String name
    ) implements SoundSetting {
    }
}
