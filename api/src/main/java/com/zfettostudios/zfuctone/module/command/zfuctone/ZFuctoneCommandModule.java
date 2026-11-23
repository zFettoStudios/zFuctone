package com.zfettostudios.zfuctone.module.command.zfuctone;

import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface ZFuctoneCommandModule extends Module<ZFuctoneCommandModule> {
    CommandConfig.@NonNull ZFuctone config();

    PermissionConfig.Command.@NonNull ZFuctone permission();
}
