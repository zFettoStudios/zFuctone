package com.zfettostudios.zfuctone.model.config.setting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class PlaceholderSetting implements EnableSetting {
    private Boolean enable;
    private String name;

    public PlaceholderSetting(String name) {
        this.enable = true;
        this.name = name;
    }
}
