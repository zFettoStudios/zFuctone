package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import io.github.miniplaceholders.api.Expansion;

public class PaperMiniPlaceholdersIntegration implements IntegrationHook<PaperMiniPlaceholdersIntegration> {
    private final IntegrationConfig.MiniPlaceholders config = ZFuctoneAPI.get().fileFacade().integration().miniplaceholders();

    private final Expansion expansion = createExpansion();

    @Override
    public PaperMiniPlaceholdersIntegration hook() {
        expansion.register();
        return this;
    }

    @Override
    public PaperMiniPlaceholdersIntegration unhook() {
        expansion.unregister();
        return this;
    }

    private Expansion createExpansion() {
        return Expansion.builder(config.identifier())
            .author(BuildConstant.PROJECT_NAME)
            .version(BuildConstant.PROJECT_VERSION)
            .build();
    }
}
