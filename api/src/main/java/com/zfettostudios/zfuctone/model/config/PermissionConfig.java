package com.zfettostudios.zfuctone.model.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.constant.world.PermissionType;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "permission.yml")
public class PermissionConfig {
    @NonFinal String name = "zfuctone.module";
    @NonFinal PermissionType type = PermissionType.TRUE;

    Command command = new Command();
    Integration integration = new Integration();
    Message message = new Message();
    Placeholder placeholder = new Placeholder();

    @Getter
    @Setter
    public static class Command {
        @NonFinal String name = "zfuctone.module.command";
        @NonFinal PermissionType type = PermissionType.TRUE;

        ZFuctone zfuctone = new ZFuctone();

        @Getter
        @Setter
        public static class ZFuctone {
            @NonFinal String name = "zfuctone.module.command.zfuctone";
            @NonFinal PermissionType type = PermissionType.OP;
            PermissionSetting cooldownBypass = new PermissionSetting("zfuctone.module.command.zfuctone.cooldown_bypass",
                PermissionType.OP);
            PermissionSetting sound = new PermissionSetting("zfuctone.module.command.zfuctone.sound", PermissionType.TRUE);
            Reload reload = new Reload();

            @Getter
            @Setter
            public static class Reload {
                @NonFinal String name = "zfuctone.module.command.zfuctone.reload";
                @NonFinal PermissionType type = PermissionType.OP;
                PermissionSetting sound = new PermissionSetting("zfuctone.module.command.zfuctone.reload.sound",
                    PermissionType.TRUE);
            }
        }
    }

    @Getter
    @Setter
    public static class Integration {
        @NonFinal String name = "zfuctone.module.integration";
        @NonFinal PermissionType type = PermissionType.TRUE;

        PlaceholderAPI placeholderapi = new PlaceholderAPI();

        @Getter
        @Setter
        public static class PlaceholderAPI {
            @NonFinal String name = "zfuctone.module.integration.placeholderapi";
            @NonFinal PermissionType type = PermissionType.TRUE;
            PermissionSetting use = new PermissionSetting("zfuctone.module.integration.placeholderapi.use", PermissionType.OP);
        }
    }

    @Getter
    @Setter
    public static class Message {
        @NonFinal String name = "zfuctone.module.message";
        @NonFinal PermissionType type = PermissionType.TRUE;
    }

    @Getter
    @Setter
    public static class Placeholder {
        @NonFinal String name = "zfuctone.module.placeholder";
        @NonFinal PermissionType type = PermissionType.TRUE;
        PermissionSetting use = new PermissionSetting("zfuctone.module.placeholder.use", PermissionType.OP);

        PlaceholderPermissionSetting projectAuthors = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_authors.use", PermissionType.OP
        );
        PlaceholderPermissionSetting projectDescription = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_description.use", PermissionType.OP
        );
        PlaceholderPermissionSetting projectWebsite = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_website.use", PermissionType.OP
        );
        PlaceholderPermissionSetting projectVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_version.use", PermissionType.OP
        );
        PlaceholderPermissionSetting projectReload = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_reload.use", PermissionType.OP
        );
        PlaceholderPermissionSetting playerLocale = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_locale.use", PermissionType.OP
        );
        PlaceholderPermissionSetting playerVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_version.use", PermissionType.OP
        );
        PlaceholderPermissionSetting playerEdition = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_edition.use", PermissionType.OP
        );
        PlaceholderPermissionSetting playerDisplayEdition = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_display_edition.use", PermissionType.OP
        );
        PlaceholderPermissionSetting playerDisplayVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_display_version.use", PermissionType.OP
        );

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @FieldDefaults(makeFinal = false)
        public static class PlaceholderPermissionSetting {
            String name;
            PermissionType type;
            PermissionSetting use = new PermissionSetting();

            public PlaceholderPermissionSetting(String name, PermissionType type) {
                use =  new PermissionSetting(name, type);
            }
        }
    }
}
