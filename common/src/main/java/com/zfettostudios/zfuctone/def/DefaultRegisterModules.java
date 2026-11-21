package com.zfettostudios.zfuctone.def;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModuleImpl;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModuleImpl;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.CustomPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.CustomPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.PlayerPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.PlayerPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.ProjectPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.ProjectPlaceholderModuleImpl;
import com.zfettostudios.zfuctone.module.placeholder.ServerPlaceholderModule;
import com.zfettostudios.zfuctone.module.placeholder.ServerPlaceholderModuleImpl;

@Deprecated(since = "0.1")
public abstract class DefaultRegisterModules {
    protected final ModuleController moduleController = ZFuctoneAPI.get().moduleController();

    public void registerModules() {
        GeyserModule geyserModule = new GeyserModuleImpl();

        moduleController.register(ViaVersionModule.class, new ViaVersionModuleImpl());
        moduleController.register(GeyserModule.class, geyserModule);
        moduleController.register(FloodgateModule.class, new FloodgateModuleImpl(geyserModule));
        moduleController.register(LuckPermsModule.class, new LuckPermsModuleImpl());
        registerMiniPlaceholders();
        registerPlaceholderAPI();

        moduleController.register(ProjectPlaceholderModule.class, new ProjectPlaceholderModuleImpl());
        moduleController.register(PlayerPlaceholderModule.class, new PlayerPlaceholderModuleImpl());
        moduleController.register(ServerPlaceholderModule.class, new ServerPlaceholderModuleImpl());
        moduleController.register(CustomPlaceholderModule.class, new CustomPlaceholderModuleImpl());
    }

    protected abstract void registerMiniPlaceholders();
    protected abstract void registerPlaceholderAPI();
}