package com.zfettostudios.zfuctone.model.config.setting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class PlaceholderValueSetting {
    private Boolean enable;
    private String value;

    public PlaceholderValueSetting(String value) {
        this.enable = true;
        this.value = value;
    }
}