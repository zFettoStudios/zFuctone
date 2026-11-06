package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import zfuctoneT.annotation.config.FilePath;
import zfuctoneT.model.config.setting.EnableSetting;
import zfuctoneT.model.config.setting.TickerConfigSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath("message.yml")
public class MessageConfig implements EnableSetting {
    @NonFinal Boolean enable;
    @JsonMerge Format format = new Format();
    @JsonMerge Join join = new Join();
    @JsonMerge Quit quit = new Quit();

    @Getter
    @Setter
    public static class Format implements EnableSetting {
        @NonFinal Boolean enable;
        @NonFinal Boolean convertLegacyColor;
        @JsonMerge Names names = new Names();

        @Getter
        @Setter
        public static class Names implements EnableSetting {
            @NonFinal Boolean enable;
            @JsonMerge TickerConfigSetting ticker = new TickerConfigSetting();
        }
    }

    @Getter
    @Setter
    public static class Join implements EnableSetting {
        @NonFinal Boolean enable;
    }

    @Getter
    @Setter
    public static class Quit implements EnableSetting {
        @NonFinal Boolean enable;
    }
}
