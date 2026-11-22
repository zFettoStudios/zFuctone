package com.zfettostudios.zfuctone.provider;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.factory.ModelFactoryImpl;
import com.zfettostudios.zfuctone.factory.PlatformPlayerFactoryImpl;
import com.zfettostudios.zfuctone.factory.UtilFactoryImpl;

@UtilityClass
public class FactoryProviderImpl extends FactoryProvider {
    public void init() {
        utilFactory = new UtilFactoryImpl();
        modelFactory = new ModelFactoryImpl();
        playerFactory = new PlatformPlayerFactoryImpl();
    }
}
