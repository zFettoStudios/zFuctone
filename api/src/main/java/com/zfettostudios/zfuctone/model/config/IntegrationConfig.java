package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "integration.yml")
public class IntegrationConfig {
    @NonFinal Boolean enable = true;
    PlaceholderAPI placeholderapi = new PlaceholderAPI();
    MiniPlaceholders miniplaceholders = new MiniPlaceholders();
    Floodgate floodgate = new Floodgate();
    Geyser geyser = new Geyser();
    ViaVersion viaversion = new ViaVersion();
    FlectonePulse flectonepulse = new FlectonePulse();
    LuckPerms luckperms = new LuckPerms();
    SkinsRestorer skinsrestorer = new SkinsRestorer();

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class PlaceholderAPI {
        Boolean enable = true;
        String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
        Boolean processNestedPlaceholders = true;
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class MiniPlaceholders {
        Boolean enable = true;
        String identifier = BuildConstant.PROJECT_NAME.toLowerCase();
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Floodgate {
        Boolean enable = true;
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Geyser {
        Boolean enable = true;
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class ViaVersion {
        Boolean enable = true;
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class SkinsRestorer {
        Boolean enable = true;
        Boolean loadMojangSkin = true;
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class LuckPerms {
        Boolean enable = true;
        Boolean useToGrantPermissions = true;
        Boolean useWeightForPermission = true;
        Boolean useWeightForTabSort = true;
        final List<String> tabGroupOrder = new LinkedList<>();
        final List<String> groupOrder = new LinkedList<>();
    }

    @Getter
    @Setter
    public static class FlectonePulse {
        @NonFinal Boolean enable = true;
        Message message = new Message();

        @Getter
        @Setter
        public static class Message {
            @NonFinal Boolean enable = true;
            ModuleSetting join = new ModuleSetting();
            ModuleSetting quit = new ModuleSetting();
        }

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class ModuleSetting {
            Boolean useFlectonePulseModule = false;
            Boolean disableFlectonePulseModule = false;
            Boolean disableZfuctoneModule = true;
        }
    }
}
