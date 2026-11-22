package com.zfettostudios.zfuctone.module.message.join;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface JoinModule extends Module<JoinModule> {
    MessageConfig.@NonNull Join config();

    PermissionConfig.Message.@NonNull Join permission();
}
