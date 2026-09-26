package com.zfettostudios.zfuctone.config.model.setting;

import com.zfettostudios.zjtime.TimeUnit;

public interface CooldownSetting extends EnableSetting {
    Long duration();
    TimeUnit timeUnit();
}
