package com.zfettostudios.zfuctone.config.model.setting;

public interface CommandPermissionSetting extends PermissionSetting {
    PermissionSetting cooldownBypass();
    PermissionSetting sound();
}
