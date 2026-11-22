package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.EnableSetting;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "message.yml")
public class MessageConfig implements EnableSetting {
    private Boolean enable = true;
    private final Format format = new Format();
    private final Join join = new Join();
    private final Quit quit = new Quit();

    @Getter
    @Setter
    public static class Format implements EnableSetting {
        private Boolean enable = true;
        private Boolean convertLegacyColor = true;
    }

    @Getter
    @Setter
    public static class Join implements EnableSetting {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class Quit implements EnableSetting {
        private Boolean enable = true;
    }
}
