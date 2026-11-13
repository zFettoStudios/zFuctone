package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "integration.yml")
public class IntegrationConfig {
    private Boolean enable = true;
    private final PlaceholderAPI placeholderapi = new PlaceholderAPI();
    private final MiniPlaceholders miniplaceholders = new MiniPlaceholders();
    private final Floodgate floodgate = new Floodgate();
    private final Geyser geyser = new Geyser();
    private final ViaVersion viaversion = new ViaVersion();
    private final FlectonePulse flectonepulse = new FlectonePulse();
    private final LuckPerms luckperms = new LuckPerms();
    private final SkinsRestorer skinsrestorer = new SkinsRestorer();

    @Getter
    @Setter
    public static class PlaceholderAPI {
        private Boolean enable = true;
        private String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
        private Boolean processNestedPlaceholders = true;
    }

    @Getter
    @Setter
    public static class MiniPlaceholders {
        private Boolean enable = true;
        private String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
    }

    @Getter
    @Setter
    public static class Floodgate {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class Geyser {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class ViaVersion {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class SkinsRestorer {
        private Boolean enable = true;
        private Boolean loadMojangSkin = true;
    }

    @Getter
    @Setter
    public static class LuckPerms {
        private Boolean enable = true;
        private Boolean useWeightForTabSort = true;
        private final List<String> tabGroupOrder = new LinkedList<>();
        private final List<String> groupOrder = new LinkedList<>();
    }

    @Getter
    @Setter
    public static class FlectonePulse {
        private Boolean enable = true;
        private final Message message = new Message();

        @Getter
        @Setter
        public static class Message {
            private Boolean enable = true;
            private final ModuleSetting join = new ModuleSetting();
            private final ModuleSetting quit = new ModuleSetting();
        }

        @Getter
        @Setter
        public static class ModuleSetting {
            private Boolean useFlectonePulseModule = false;
            private Boolean disableFlectonePulseModule = false;
            private Boolean disableZfuctoneModule = true;
        }
    }
}
