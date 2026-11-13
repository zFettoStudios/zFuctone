package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholdersapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;

public interface Integration extends Module<Integration> {
    PlaceholderAPIModule placeholderAPI();
    MiniPlaceholdersModule miniPlaceholders();
    ViaVersionModule viaVersion();
    FloodgateModule floodgate();
    GeyserModule geyser();

    boolean isEnable();
}
