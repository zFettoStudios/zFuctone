package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.constant.world.PermissionType;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "permission.yml")
public class PermissionConfig {
    private String name = "zfuctone.module";
    private PermissionType type = PermissionType.TRUE;

    private final Command command = new Command();
    private final Integration integration = new Integration();
    private final Message message = new Message();
    private final Placeholder placeholder = new Placeholder();

    @Getter
    @Setter
    public static class Command {
        private String name = "zfuctone.module.command";
        private PermissionType type = PermissionType.TRUE;

        private final ZFuctone zfuctone = new ZFuctone();

        @Getter
        @Setter
        public static class ZFuctone {
            private String name = "zfuctone.module.command.zfuctone";
            private PermissionType type = PermissionType.OP;
            private final PermissionSetting cooldownBypass = new PermissionSetting(
                "zfuctone.module.command.zfuctone.cooldown_bypass", PermissionType.OP);
            private final PermissionSetting sound = new PermissionSetting(
                "zfuctone.module.command.zfuctone.sound", PermissionType.TRUE);
            private final Reload reload = new Reload();

            @Getter
            @Setter
            public static class Reload {
                private String name = "zfuctone.module.command.zfuctone.reload";
                private PermissionType type = PermissionType.OP;
                private final PermissionSetting sound = new PermissionSetting(
                    "zfuctone.module.command.zfuctone.reload.sound", PermissionType.TRUE);
            }
        }
    }

    @Getter
    @Setter
    public static class Integration {
        private String name = "zfuctone.module.integration";
        private PermissionType type = PermissionType.TRUE;

        private final PlaceholderAPI placeholderapi = new PlaceholderAPI();

        @Getter
        @Setter
        public static class PlaceholderAPI {
            private String name = "zfuctone.module.integration.placeholderapi";
            private PermissionType type = PermissionType.TRUE;
            private final PermissionSetting use = new PermissionSetting(
                "zfuctone.module.integration.placeholderapi.use", PermissionType.OP);
        }
    }

    @Getter
    @Setter
    public static class Message {
        private String name = "zfuctone.module.message";
        private PermissionType type = PermissionType.TRUE;
    }

    @Getter
    @Setter
    public static class Placeholder {
        private String name = "zfuctone.module.placeholder";
        private PermissionType type = PermissionType.TRUE;
        private final PermissionSetting use = new PermissionSetting(
            "zfuctone.module.placeholder.use", PermissionType.OP);

        private final PlaceholderPermissionSetting projectAuthors = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_authors.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting projectDescription = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_description.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting projectWebsite = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_website.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting projectVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_version.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting projectReload = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.project_reload.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting playerLocale = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_locale.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting playerVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_version.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting playerEdition = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_edition.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting playerDisplayEdition = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_display_edition.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting playerDisplayVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.player_display_version.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting serverType = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.server_type.use", PermissionType.OP
        );
        private final PlaceholderPermissionSetting serverVersion = new PlaceholderPermissionSetting(
            "zfuctone.module.placeholder.server_version.use", PermissionType.OP
        );

        @Getter
        @Setter
        public static class PlaceholderPermissionSetting {
            private String name;
            private PermissionType type;
            private final PermissionSetting use;

            public PlaceholderPermissionSetting(String name, PermissionType type) {
                use = new PermissionSetting(name, type);
            }

            public PlaceholderPermissionSetting() {
                use = new PermissionSetting();
            }
        }
    }
}
