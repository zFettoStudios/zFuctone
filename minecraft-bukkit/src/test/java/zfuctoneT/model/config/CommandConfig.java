package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import zfuctoneT.annotation.config.FilePath;
import zfuctoneT.model.config.setting.CommandSetting;
import zfuctoneT.model.config.setting.CooldownSetting;
import zfuctoneT.model.config.setting.EnableSetting;
import zfuctoneT.model.config.setting.SoundSetting;
import zfuctoneT.model.config.type.SoundCategory;
import com.zfettostudios.zjtime.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath("command.yml")
public class CommandConfig implements EnableSetting {
    @NonFinal Boolean enable;
    @JsonMerge ZFuctone zfuctone = new ZFuctone();
    @JsonMerge Spawn spawn = new Spawn();
    @JsonMerge SetSpawn setspawn = new SetSpawn();

    @Getter
    @Setter
    public static class ZFuctone implements CommandSetting {
        @NonFinal Boolean enable;
        @NonFinal List<String> aliases = new CopyOnWriteArrayList<>();
        @JsonMerge Cooldown cooldown = new Cooldown();
        @JsonMerge Sound sound = new Sound();

        @JsonMerge Reload reload = new Reload();

        @Getter
        @Setter
        public static class Reload implements CommandSetting {
            @NonFinal Boolean enable;
            @NonFinal List<String> aliases = new CopyOnWriteArrayList<>();
            @JsonMerge Cooldown cooldown = new Cooldown();
            @JsonMerge Sound sound = new Sound();
        }
    }

    @Getter
    @Setter
    public static class Spawn implements CommandSetting {
        @NonFinal Boolean enable;
        @NonFinal List<String> aliases = new CopyOnWriteArrayList<>();
        @JsonMerge Cooldown cooldown = new Cooldown();
        @JsonMerge Sound sound = new Sound();

        @JsonMerge Other other = new Other();

        @Getter
        @Setter
        public static class Other {
            @NonFinal Boolean enable;
            @JsonMerge Cooldown cooldown = new Cooldown();
            @JsonMerge Sound sound = new Sound();
        }
    }

    @Getter
    @Setter
    public static class SetSpawn implements CommandSetting {
        @NonFinal Boolean enable;
        @NonFinal List<String> aliases = new CopyOnWriteArrayList<>();
        @JsonMerge Cooldown cooldown = new Cooldown();
        @JsonMerge Sound sound = new Sound();
    }

    @Getter
    @Setter
    public static class Cooldown implements CooldownSetting {
        @NonFinal Boolean enable;
        @NonFinal Long duration;
        @NonFinal TimeUnit timeUnit;
    }

    @Getter
    @Setter
    public static class Sound implements SoundSetting {
        @NonFinal Boolean enable;
        @NonFinal Float volume;
        @NonFinal Float pitch;
        @NonFinal SoundCategory category;
        @NonFinal String name;
    }
}
