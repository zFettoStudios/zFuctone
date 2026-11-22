package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.EnableSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "integration.yml")
public class IntegrationConfig implements EnableSetting {
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
    public static class PlaceholderAPI implements EnableSetting {
        private Boolean enable = true;
        private String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
        private Boolean processNestedPlaceholders = true;
    }

    @Getter
    @Setter
    public static class MiniPlaceholders implements EnableSetting {
        private Boolean enable = true;
        private String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
    }

    @Getter
    @Setter
    public static class Floodgate implements EnableSetting {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class Geyser implements EnableSetting {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class ViaVersion implements EnableSetting {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class SkinsRestorer implements EnableSetting {
        private Boolean enable = true;
        private Boolean loadMojangSkin = true;
    }

    @Getter
    @Setter
    public static class LuckPerms implements EnableSetting {
        private Boolean enable = true;
        private Boolean useWeightForTabSort = true;
        private final List<String> tabGroupOrder = new CopyOnWriteArrayList<>();
        private final List<String> groupOrder = new CopyOnWriteArrayList<>();
    }

    @Getter
    @Setter
    public static class FlectonePulse implements EnableSetting {
        private Boolean enable = true;
        private final Message message = new Message();

        @Getter
        @Setter
        public static class Message implements EnableSetting {
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
