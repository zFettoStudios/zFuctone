package com.zfettostudios.zfuctone.module.message;

import com.zfettostudios.zfuctone.module.Module;

public interface MessageModule<T> extends Module<T> {
    T register();
    T unregister();
}
