package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.java.PlatformImpl;
import com.zfettostudios.zfuctone.util.forload.ZFuctoneInitiator;

import java.lang.reflect.Method;

public class Launcher {
    static void main(String[] args) {
        ZFuctoneInitiator.init(Launcher.class.getClassLoader(), new PlatformImpl());

        try {
            Class<?> clazz = Class.forName("com.zfettostudios.zfuctone.util.ConsoleLogic");

            Method runMethod = clazz.getMethod("run", String[].class);
            runMethod.invoke(null, (Object) args);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
