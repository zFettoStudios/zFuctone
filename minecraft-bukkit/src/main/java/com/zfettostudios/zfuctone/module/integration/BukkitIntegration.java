package com.zfettostudios.zfuctone.module.integration;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.module.integration.miniplaceholders.BukkitMiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.BukkitPlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.integration.placeholdersapi.PlaceholderAPIModule;

@Getter
@Accessors(fluent = true)
public class BukkitIntegration extends IntegrationImpl {
    private final PlaceholderAPIModule placeholderAPI = new BukkitPlaceholderAPIModule();
    private final MiniPlaceholdersModule miniPlaceholders = new BukkitMiniPlaceholdersModule();

    @Override
    protected Integration platformEnable() {
        placeholderAPI.enable();
        miniPlaceholders.enable();
        return this;
    }

    @Override
    protected Integration platformDisable() {
        placeholderAPI.disable();
        miniPlaceholders.disable();
        return this;
    }
}
