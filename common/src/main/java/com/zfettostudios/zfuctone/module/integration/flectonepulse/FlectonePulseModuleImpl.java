package com.zfettostudios.zfuctone.module.integration.flectonepulse;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import net.flectone.pulse.FlectonePulse;
import net.flectone.pulse.FlectonePulseAPI;
import net.flectone.pulse.module.message.join.JoinModule;
import net.flectone.pulse.module.message.quit.QuitModule;
import net.flectone.pulse.platform.controller.ModuleController;
import org.jspecify.annotations.NonNull;

public class FlectonePulseModuleImpl extends IntegrationModuleImpl<FlectonePulseModule> implements FlectonePulseModule {
    @Override
    public String getName() {
        return "FlectonePulse";
    }

    @Override
    public @NonNull FlectonePulseModule get() {
        return this;
    }

    @Override
    public IntegrationConfig.@NonNull FlectonePulse config() {
        return ZFuctoneProvider.get().fileFacade().integration().flectonepulse();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().flectonepulse();
    }

    @Override
    public @NonNull Class<? extends Module<FlectonePulseModule>> moduleClass() {
        return FlectonePulseModule.class;
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.register(moduleClass(), permission().name(), permission().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.unregisterAll(moduleClass());
    }

    @Override
    public @NonNull FlectonePulseModule disableJoinModule() {
        if (isDisable() || !config().message().enable() || !config().message().join().disableFlectonepulseModule()) return this;

        FlectonePulse flectonePulse = FlectonePulseAPI.getInstance();

        flectonePulse.get(ModuleController.class).disable(flectonePulse.get(JoinModule.class));

        return this;
    }

    @Override
    public @NonNull FlectonePulseModule disableQuitModule() {
        if (isDisable() || !config().message().enable() || !config().message().quit().disableFlectonepulseModule()) return this;

        FlectonePulse flectonePulse = FlectonePulseAPI.getInstance();

        flectonePulse.get(ModuleController.class).disable(flectonePulse.get(QuitModule.class));

        return this;
    }

    @Override
    protected void onEnable() {
        if (config().message().enable() && config().message().join().disableFlectonepulseModule()) disableJoinModule();
        if (config().message().enable() && config().message().join().disableFlectonepulseModule()) disableQuitModule();
    }

    @Override
    public boolean shouldZFuctoneQuitModuleDisable() {
        return isEnable() && config().message().enable() && config().message().quit().disableZfuctoneModule();
    }

    @Override
    public boolean shouldZFuctoneJoinModuleDisable() {
        return isEnable() && config().message().enable() && config().message().join().disableZfuctoneModule();
    }
}
