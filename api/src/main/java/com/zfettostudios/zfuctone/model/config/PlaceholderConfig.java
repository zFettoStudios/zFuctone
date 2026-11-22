package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.EnableSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderValueSetting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "placeholder.yml")
public class PlaceholderConfig implements EnableSetting {
    private Boolean enable = true;
    private final Map<String, PlaceholderValueSetting> values = new ConcurrentHashMap<>();
    private final Standard standard = new Standard();

    @Getter
    @Setter
    public static class Standard implements EnableSetting {
        private Boolean enable = true;

        private final PlaceholderSetting projectAuthors = new PlaceholderSetting("project_authors");
        private final PlaceholderSetting projectDescription = new PlaceholderSetting("project_description");
        private final PlaceholderSetting projectWebsite = new PlaceholderSetting("project_website");
        private final PlaceholderSetting projectVersion = new PlaceholderSetting("project_version");
        private final PlaceholderSetting projectReload = new PlaceholderSetting("project_reload");

        private final PlaceholderSetting serverType = new PlaceholderSetting("server_type");
        private final PlaceholderSetting serverVersion = new PlaceholderSetting("server_version");
        private final PlaceholderSetting serverPlayersOnline = new PlaceholderSetting("server_players_online");
        private final PlaceholderSetting serverPlayersMax = new PlaceholderSetting("server_players_max");
        private final PlaceholderSetting serverTps1 = new PlaceholderSetting("server_tps_1");
        private final PlaceholderSetting serverTps5 = new PlaceholderSetting("server_tps_5");
        private final PlaceholderSetting serverTps15 = new PlaceholderSetting("server_tps_15");

        private final PlaceholderSetting playerNickname = new PlaceholderSetting("player_nickname");
        private final PlaceholderSetting playerUuid = new PlaceholderSetting("player_uuid");
        private final PlaceholderSetting playerIp = new PlaceholderSetting("player_ip");
        private final PlaceholderSetting playerLocale = new PlaceholderSetting("player_locale");
        private final PlaceholderSetting playerPing = new PlaceholderSetting("player_ping");
        private final PlaceholderSetting playerProtocolVersion = new PlaceholderSetting("player_protocol_version");
        private final PlaceholderSetting playerVersion = new PlaceholderSetting("player_version");
        private final PlaceholderSetting playerDisplayVersion = new PlaceholderSetting("player_display_version");
        private final PlaceholderSetting playerEdition = new PlaceholderSetting("player_edition");
        private final PlaceholderSetting playerDisplayEdition = new PlaceholderSetting("player_display_edition");
        private final PlaceholderSetting playerGamemode = new PlaceholderSetting("player_gamemode");
        private final PlaceholderSetting playerWorldName = new PlaceholderSetting("player_world_name");
        private final PlaceholderSetting playerWorldEnvironment = new PlaceholderSetting("player_world_environment");
        private final PlaceholderSetting playerHasPermission = new PlaceholderSetting("player_has_permission_<permission>");
        private final PlaceholderSetting playerIsOperator = new PlaceholderSetting("player_is_operator");
        private final PlaceholderSetting playerIsSneaking = new PlaceholderSetting("player_is_sneaking");
        private final PlaceholderSetting playerIsDead = new PlaceholderSetting("player_is_dead");
    }
}