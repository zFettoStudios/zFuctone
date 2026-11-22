package com.zfettostudios.zfuctone.module.message.quit;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface QuitModule extends Module<QuitModule> {
    MessageConfig.@NonNull Quit config();

    PermissionConfig.Message.@NonNull Quit permission();
}
