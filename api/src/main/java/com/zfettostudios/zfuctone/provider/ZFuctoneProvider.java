package com.zfettostudios.zfuctone.provider;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctone;

@UtilityClass
public class ZFuctoneProvider {
    private ZFuctone instance;

    public ZFuctone get() {
        return instance;
    }

    public ZFuctone set(@NonNull ZFuctone zfuctone) {
        if (instance != null && instance.isEnable()) instance.logger().warn("The zFuctone kernel was overwritten while it was running!");

        return instance = zfuctone;
    }

    public boolean isReady() {
        return instance != null;
    }

}