package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.factory.ModelFactory;
import com.zfettostudios.zfuctone.factory.PlatformPlayerFactory;
import com.zfettostudios.zfuctone.factory.UtilFactory;
import org.jspecify.annotations.NonNull;

public abstract class FactoryProvider {
    protected static ModelFactory modelFactory;
    protected static UtilFactory utilFactory;
    protected static PlatformPlayerFactory playerFactory;

    public static @NonNull ModelFactory modelFactory() {
        return modelFactory;
    }

    public static @NonNull UtilFactory utilFactory() {
        return utilFactory;
    }

    public static @NonNull PlatformPlayerFactory playerFactory() {
        return playerFactory;
    }
}
