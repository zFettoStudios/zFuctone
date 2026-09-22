package com.zfettostudios.zfuctone.util.forload;

import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.model.platform.PlatformType;
import com.zfettostudios.zfuctone.util.inject.ClassLoaderInjector;
import com.zfettostudios.zfuctone.util.library.DefaultLibrariesSetup;
import com.zfettostudios.zfuctone.util.library.LibraryManager;

import java.lang.reflect.Method;

public class ZFuctoneInitiator {
    private static Class<?> zfuctoneSwitch;

    public static void init(ClassLoader classLoader, Platform platform) {
        switch (platform.getType()) {
            case BUKKIT -> new ClassLoaderInjector(classLoader, false);
            case JAVA -> new ClassLoaderInjector(classLoader, true);
        }

        DefaultLibrariesSetup defaultLibrariesSetup = new DefaultLibrariesSetup(platform);
        defaultLibrariesSetup.init();

        try {
            zfuctoneSwitch = Class.forName("com.zfettostudios.zfuctone.util.forload.ZFuctoneSwitch");

            Method runMethod = zfuctoneSwitch.getMethod("start", Platform.class, LibraryManager.class);
            runMethod.invoke(null, platform, defaultLibrariesSetup.getLibraryManager());

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void disable() {
        if (zfuctoneSwitch == null) return;

        try {
            Method runMethod = zfuctoneSwitch.getMethod("disable");
            runMethod.invoke(null);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
