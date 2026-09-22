package com.zfettostudios.zfuctone.util.forload;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.util.library.LibraryManager;

public class ZFuctoneSwitch {
    public static void start(Platform platform, LibraryManager libraryManager) {
        new ZFuctone(platform, libraryManager).start();
    }

    public static void disable() {
        ZFuctone.getInstance().disable();
    }
}
