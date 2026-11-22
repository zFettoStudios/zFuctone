package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.CooldownSetting;
import com.zfettostudios.zfuctone.model.config.setting.DestinationSetting;
import com.zfettostudios.zfuctone.model.config.setting.EnableSetting;
import com.zfettostudios.zfuctone.model.config.setting.SoundSetting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "command.yml")
public class CommandConfig implements EnableSetting {
    private Boolean enable = true;
    private final ZFuctone zfuctone = new ZFuctone();

    @Getter
    @Setter
    public static class ZFuctone implements EnableSetting {
        private Boolean enable = true;
        private final List<String> aliases = new CopyOnWriteArrayList<>();
        private final DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
        private final CooldownSetting cooldown = new CooldownSetting();
        private final SoundSetting sound = new SoundSetting();
        private final Reload reload = new Reload();

        public ZFuctone() {
            aliases.add("zfuctone");
        }

        @Getter
        @Setter
        public static class Reload implements EnableSetting {
            private Boolean enable = true;
            private final List<String> aliases = new CopyOnWriteArrayList<>();
            private final DestinationSetting destination = DestinationSetting.builder().type(DestinationSetting.Type.CHAT).build();
            private final CooldownSetting cooldown = new CooldownSetting();
            private final SoundSetting sound = new SoundSetting();

            public Reload() {
                aliases.add("reload");
            }
        }
    }
}
