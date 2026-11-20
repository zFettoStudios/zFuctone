package com.zfettostudios.zfuctone.provider;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.placeholder.PlaceholderContextImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayerImpl;

@UtilityClass
public class BuilderProviderImpl extends BuilderProvider {
    public void init() {
        buildPlatformPlayerFromUUID = PlatformPlayerImpl::build;
        buildPlatformPlayerFromRawPlayer = PlatformPlayerImpl::build;
        buildPlaceholderContext = PlaceholderContextImpl::new;
        buildPlaceholderContextFromRawText = PlaceholderContextImpl::build;
    }
}
