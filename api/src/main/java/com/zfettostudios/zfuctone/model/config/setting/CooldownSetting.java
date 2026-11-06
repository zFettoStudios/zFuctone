package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(makeFinal = false)
public class CooldownSetting {
    Boolean enable = false;
    Long duration = 20L;
}
