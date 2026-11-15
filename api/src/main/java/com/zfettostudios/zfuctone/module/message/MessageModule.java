package com.zfettostudios.zfuctone.module.message;

import com.zfettostudios.zfuctone.module.ReloadModule;

public interface MessageModule<T> extends ReloadModule<T> {
    T register();
    T unregister();

    T get();
}
