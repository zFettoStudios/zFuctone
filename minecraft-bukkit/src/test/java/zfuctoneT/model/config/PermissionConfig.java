package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import zfuctoneT.annotation.config.FilePath;
import zfuctoneT.model.config.setting.CommandPermissionSetting;
import zfuctoneT.model.config.setting.PermissionSetting;
import zfuctoneT.model.config.type.PermissionType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Accessors(fluent = true)
@FilePath("permission.yml")
public class PermissionConfig {
    @JsonMerge Command command = new Command();

    @Getter
    public static class Command{
        @JsonMerge ZFuctone zfuctone = new ZFuctone();
        @JsonMerge Spawn spawn = new Spawn();
        @JsonMerge SetSpawn setspawn = new SetSpawn();

        @Getter
        @Setter
        public static class ZFuctone implements CommandPermissionSetting {
            @NonFinal String name;
            @NonFinal PermissionType type;
            @JsonMerge PermissionSection cooldownBypass = new PermissionSection();
            @JsonMerge PermissionSection sound = new PermissionSection();

            @JsonMerge Reload reload = new Reload();

            @Getter
            @Setter
            public static class Reload implements CommandPermissionSetting {
                @NonFinal String name;
                @NonFinal PermissionType type;
                @JsonMerge PermissionSection cooldownBypass = new PermissionSection();
                @JsonMerge PermissionSection sound = new PermissionSection();
            }
        }

        @Getter
        @Setter
        public static class Spawn implements CommandPermissionSetting {
            @NonFinal String name;
            @NonFinal PermissionType type;
            @JsonMerge PermissionSection cooldownBypass = new PermissionSection();
            @JsonMerge PermissionSection sound = new PermissionSection();

            @JsonMerge Other other = new Other();

            @Getter
            @Setter
            public static class Other implements CommandPermissionSetting {
                @NonFinal String name;
                @NonFinal PermissionType type;
                @JsonMerge PermissionSection cooldownBypass = new PermissionSection();
                @JsonMerge PermissionSection sound = new PermissionSection();
            }
        }

        @Getter
        @Setter
        public static class SetSpawn implements CommandPermissionSetting {
            @NonFinal String name;
            @NonFinal PermissionType type;
            @JsonMerge PermissionSection cooldownBypass = new PermissionSection();
            @JsonMerge PermissionSection sound = new PermissionSection();
        }
    }

    @Getter
    @Setter
    public static class PermissionSection implements PermissionSetting {
        @NonFinal String name;
        @NonFinal PermissionType type;
    }
}
