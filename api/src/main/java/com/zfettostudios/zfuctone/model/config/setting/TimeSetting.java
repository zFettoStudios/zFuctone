package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import com.zfettostudios.zjtime.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(makeFinal = false)
public class TimeSetting {
    Long duration;
    TimeUnit timeUnit;
}
