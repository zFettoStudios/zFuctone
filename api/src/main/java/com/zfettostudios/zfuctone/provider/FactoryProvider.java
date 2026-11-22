package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.factory.ModelFactory;
import com.zfettostudios.zfuctone.factory.PlatformPlayerFactory;
import com.zfettostudios.zfuctone.factory.UtilFactory;

public abstract class FactoryProvider {
    protected static ModelFactory modelFactory;
    protected static UtilFactory utilFactory;
    protected static PlatformPlayerFactory playerFactory;

    public static ModelFactory modelFactory() {
        return modelFactory;
    }

    public static UtilFactory utilFactory() {
        return utilFactory;
    }

    public static PlatformPlayerFactory playerFactory() {
        return playerFactory;
    }
}
