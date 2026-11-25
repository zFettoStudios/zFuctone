package com.zfettostudios.zfuctone.module.integration.flectonepulse;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface FlectonePulseModule extends Module<FlectonePulseModule> {
    IntegrationConfig.@NonNull FlectonePulse config();

    @NonNull FlectonePulseModule disableJoinModule();
    @NonNull FlectonePulseModule disableQuitModule();

    boolean shouldZFuctoneQuitModuleDisable();
    boolean shouldZFuctoneJoinModuleDisable();
}
