package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import com.zfettostudios.zfuctone.model.constant.world.SoundCategory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(makeFinal = false)
public class SoundSetting {
    Boolean enable = false;
    Float volume;
    Float pitch;
    SoundCategory category;
    String name;
}
