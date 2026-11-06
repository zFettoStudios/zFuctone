package com.zfettostudios.zfuctone.model.constant.time;

import com.zfettostudios.zjtime.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUnits {
    public static final TimeUnit TICKS = TimeUnit.create(50L, TimeUnit.MILLISECONDS, "TICKS", "ticks");
}
