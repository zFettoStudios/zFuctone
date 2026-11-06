package zfuctoneT.model.config.setting;

public interface CommandPermissionSetting extends PermissionSetting {
    PermissionSetting cooldownBypass();
    PermissionSetting sound();
}
