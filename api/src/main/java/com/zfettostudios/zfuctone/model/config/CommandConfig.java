package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.CooldownSetting;
import com.zfettostudios.zfuctone.model.config.setting.DestinationSetting;
import com.zfettostudios.zfuctone.model.config.setting.SoundSetting;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "command.yml")
public class CommandConfig {
    @NonFinal Boolean enable = true;
    ZFuctone zfuctone = new ZFuctone();

    @Getter
    @Setter
    public static class ZFuctone {
        @NonFinal Boolean enable = true;
        List<String> aliases = new LinkedList<>();
        DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
        CooldownSetting cooldown = new CooldownSetting();
        SoundSetting sound = new SoundSetting();
        Reload reload = new Reload();

        public ZFuctone() {
            aliases.add("zfuctone");
        }

        @Getter
        @Setter
        public static class Reload {
            @NonFinal Boolean enable = true;
            List<String> aliases = new LinkedList<>();
            DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
            CooldownSetting cooldown = new CooldownSetting();
            SoundSetting sound = new SoundSetting();

            public Reload() {
                aliases.add("reload");
            }
        }
    }
}
