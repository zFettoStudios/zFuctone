package com.zfettostudios.zfuctone.module.integration;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModuleImpl;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModuleImpl;

@Getter
@Accessors(fluent = true)
public abstract class IntegrationImpl implements Integration {
    private final ViaVersionModule viaVersion = new ViaVersionModuleImpl();
    private final GeyserModule geyser = new GeyserModuleImpl();
    private final FloodgateModule floodgate = new FloodgateModuleImpl(geyser);

    @Override
    public Integration enable() {
        if (!isEnable()) return this;
        return platformEnable();
    }

    @Override
    public Integration disable() {
        return platformDisable();
    }

    protected abstract Integration platformEnable();
    protected abstract Integration platformDisable();

    @Override
    public boolean isEnable() {
        return ZFuctoneAPI.get().fileFacade().integration().enable();
    }
}
