package com.zfettostudios.zfuctone.config.model.setting;

import java.util.List;

public interface CommandSetting extends EnableSetting {
    List<String> aliases();
    CooldownSetting cooldown();
    SoundSetting sound();
}
