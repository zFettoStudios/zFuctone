package com.zfettostudios.zfuctone.def;

import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.PaperMiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.BukkitPlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;

@Deprecated(since = "0.1")
public class BukkitDefaultRegisterModules extends DefaultRegisterModules {
    @Override
    protected void registerMiniPlaceholders() {
        moduleController.register(PlaceholderAPIModule.class, new BukkitPlaceholderAPIModule());
    }

    @Override
    protected void registerPlaceholderAPI() {
        moduleController.register(MiniPlaceholdersModule.class, new PaperMiniPlaceholdersModule());
    }
}
