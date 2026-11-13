package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.constant.world.SoundCategory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class SoundSetting {
    private Boolean enable = false;
    private Float volume;
    private Float pitch;
    private SoundCategory category;
    private String name;
}
