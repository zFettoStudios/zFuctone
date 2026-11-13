package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    private Boolean enable = true;
    private final ZFuctone zfuctone = new ZFuctone();

    @Getter
    @Setter
    public static class ZFuctone {
        private Boolean enable = true;
        private final List<String> aliases = new LinkedList<>();
        private final DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
        private final CooldownSetting cooldown = new CooldownSetting();
        private final SoundSetting sound = new SoundSetting();
        private final Reload reload = new Reload();

        public ZFuctone() {
            aliases.add("zfuctone");
        }

        @Getter
        @Setter
        public static class Reload {
            private Boolean enable = true;
            private final List<String> aliases = new LinkedList<>();
            private final DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
            private final CooldownSetting cooldown = new CooldownSetting();
            private final SoundSetting sound = new SoundSetting();

            public Reload() {
                aliases.add("reload");
            }
        }
    }
}
