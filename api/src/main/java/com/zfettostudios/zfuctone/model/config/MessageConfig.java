package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.annotation.config.FilePath;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "message.yml")
public class MessageConfig {
    @NonFinal Boolean enable = true;
    Format format = new Format();

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Format {
        Boolean enable = true;
        Boolean convertLegacyColor = true;
    }
}
