package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;

import java.util.LinkedHashMap;
import java.util.Map;

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
        private final Map<String, PermissionSetting> values = new LinkedHashMap<>();
        private final Standard standard = new Standard();

        @Getter
        @Setter
        public static class Standard {
            private final PermissionSetting projectAuthorsUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.project_authors.use", PermissionType.OP
            );
            private final PermissionSetting projectDescriptionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.project_description.use", PermissionType.OP
            );
            private final PermissionSetting projectWebsiteUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.project_website.use", PermissionType.OP
            );
            private final PermissionSetting projectVersionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.project_version.use", PermissionType.OP
            );
            private final PermissionSetting projectReloadUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.project_reload.use", PermissionType.OP
            );

            private final PermissionSetting serverTypeUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_type.use", PermissionType.OP
            );
            private final PermissionSetting serverVersionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_version.use", PermissionType.OP
            );
            private final PermissionSetting serverPlayersOnlineUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_players_online.use", PermissionType.OP
            );
            private final PermissionSetting serverPlayersMaxUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_players_max.use", PermissionType.OP
            );
            private final PermissionSetting serverTps1Use = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_tps_1.use", PermissionType.OP
            );
            private final PermissionSetting serverTps5Use = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_tps_5.use", PermissionType.OP
            );
            private final PermissionSetting serverTps15Use = new PermissionSetting(
                "zfuctone.module.placeholder.standard.server_tps_15.use", PermissionType.OP
            );

            private final PermissionSetting playerNicknameUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_nickname.use", PermissionType.OP
            );
            private final PermissionSetting playerUuidUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_uuid.use", PermissionType.OP
            );
            private final PermissionSetting playerIpUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_ip.use", PermissionType.OP
            );
            private final PermissionSetting playerLocaleUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_locale.use", PermissionType.OP
            );
            private final PermissionSetting playerPingUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_ping.use", PermissionType.OP
            );
            private final PermissionSetting playerProtocolVersionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_protocol_version.use", PermissionType.OP
            );
            private final PermissionSetting playerVersionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_version.use", PermissionType.OP
            );
            private final PermissionSetting playerDisplayVersionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_display_version.use", PermissionType.OP
            );
            private final PermissionSetting playerEditionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_edition.use", PermissionType.OP
            );
            private final PermissionSetting playerDisplayEditionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_display_edition.use", PermissionType.OP
            );
            private final PermissionSetting playerGamemodeUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_gamemode.use", PermissionType.OP
            );
            private final PermissionSetting playerWorldNameUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_world_name.use", PermissionType.OP
            );
            private final PermissionSetting playerWorldEnvironmentUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_world_environment.use", PermissionType.OP
            );
            private final PermissionSetting playerHasPermissionUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_has_permission.use", PermissionType.OP
            );
            private final PermissionSetting playerIsOperatorUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_is_operator.use", PermissionType.OP
            );
            private final PermissionSetting playerIsSneakingUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_is_sneaking.use", PermissionType.OP
            );
            private final PermissionSetting playerIsDeadUse = new PermissionSetting(
                "zfuctone.module.placeholder.standard.player_is_dead.use", PermissionType.OP
            );
        }
    }
}
