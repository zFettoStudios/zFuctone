package com.zfettostudios.zfuctone.module.message;

public interface MessageModule<T> {
    T register();
    T unregister();

    T get();
}
