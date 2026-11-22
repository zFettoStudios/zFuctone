package com.zfettostudios.zfuctone.model.config.setting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.value.ValueString;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true)
public class PlaceholderValueSetting implements EnableSetting {
    private Boolean enable;
    private ValueString value;

    public PlaceholderValueSetting(ValueString value) {
        this.enable = true;
        this.value = value;
    }
}