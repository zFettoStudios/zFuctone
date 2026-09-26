package com.zfettostudios.zfuctone.config.model.setting;

import org.bukkit.SoundCategory;

public interface SoundSetting extends EnableSetting {
    Float volume();
    Float pitch();
    SoundCategory category();
    String name();
}
