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

    @Getter
    @Setter
    public static class Format {
        private Boolean enable = true;
        private Boolean convertLegacyColor = true;
    }
}
