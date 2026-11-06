package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import zfuctoneT.annotation.config.FilePath;
import zfuctoneT.model.config.setting.EnableSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath("integration.yml")
public class IntegrationConfig implements EnableSetting {
    @NonFinal Boolean enable;
    @JsonMerge PlaceholderAPI placeholderapi = new PlaceholderAPI();
    @JsonMerge MiniPlaceholders miniplaceholders = new MiniPlaceholders();
    @JsonMerge Floodgate floodgate = new Floodgate();
    @JsonMerge ViaVersion viaversion = new ViaVersion();
    @JsonMerge FlectonePulse flectonepulse = new FlectonePulse();

    @Getter
    @Setter
    public static class PlaceholderAPI implements EnableSetting {
        @NonFinal Boolean enable;
        @NonFinal String identifier;
        @NonFinal Boolean processNestedPlaceholders;
    }

    @Getter
    @Setter
    public static class MiniPlaceholders implements EnableSetting {
        @NonFinal Boolean enable;
        @NonFinal String identifier;
    }

    @Getter
    @Setter
    public static class Floodgate implements EnableSetting {
        @NonFinal Boolean enable;
    }

    @Getter
    @Setter
    public static class ViaVersion implements EnableSetting {
        @NonFinal Boolean enable;
    }

    @Getter
    @Setter
    public static class FlectonePulse implements EnableSetting {
        @NonFinal Boolean enable;
        @JsonMerge Message message = new Message();

        @Getter
        @Setter
        public static class Message implements EnableSetting {
            @NonFinal Boolean enable;
            @JsonMerge ModuleSetting join = new ModuleSetting();
            @JsonMerge ModuleSetting quit = new ModuleSetting();
        }

        @Getter
        @Setter
        public static class ModuleSetting {
            @NonFinal Boolean useFlectonePulseModule;
            @NonFinal Boolean disableFlectonePulseModule;
            @NonFinal Boolean disableZfuctoneModule;
        }
    }
}
