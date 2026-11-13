package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "message.yml")
public class MessageConfig {
    private Boolean enable = true;
    private final Format format = new Format();
    private final Join join = new Join();
    private final Quit quit = new Quit();

    @Getter
    @Setter
    public static class Format {
        private Boolean enable = true;
        private Boolean convertLegacyColor = true;
    }

    @Getter
    @Setter
    public static class Join {
        private Boolean enable = true;
    }

    @Getter
    @Setter
    public static class Quit {
        private Boolean enable = true;
    }
}
