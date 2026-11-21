package com.zfettostudios.zfuctone.provider;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.model.value.ValueStringImpl;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.module.integration.IntegrationHookEmpty;
import com.zfettostudios.zfuctone.model.value.PlaceholderContextImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayerImpl;

@UtilityClass
public class BuilderProviderImpl extends BuilderProvider {
    private final IntegrationHook<?> integrationHookEmpty = new IntegrationHookEmpty();

    public void init() {
        buildPlatformPlayerFromUUID = PlatformPlayerImpl::build;
        buildPlatformPlayerFromRawPlayer = PlatformPlayerImpl::build;
        buildPlaceholderContext = PlaceholderContextImpl::new;
        buildPlaceholderContextFromRawText = PlaceholderContextImpl::build;
        getEmptyIntegrationHook = () -> integrationHookEmpty;
        buildValueString = ValueStringImpl::new;
        buildValueStringFromList = ValueStringImpl::new;
        buildValueStringFromString = ValueStringImpl::new;
    }
}
