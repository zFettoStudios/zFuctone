package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;

import java.util.function.Function;

public class ProjectPlaceholderModuleImpl implements ProjectPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneAPI.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneAPI.get().permissionRegistry();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();

    private final PlaceholderConfig.Standard config = fileFacade.placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = fileFacade.permission().placeholder().standard();

    @Override
    public ProjectPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public ProjectPlaceholderModule enable() {
        if (!config.enable()) return this;

        register(config.projectAuthors(), permissionConfig.projectAuthorsUse(), _ -> BuildConstant.PROJECT_AUTHORS);
        register(config.projectDescription(), permissionConfig.projectDescriptionUse(), _ -> BuildConstant.PROJECT_DESCRIPTION);
        register(config.projectWebsite(), permissionConfig.projectWebsiteUse(), _ -> BuildConstant.PROJECT_WEBSITE);
        register(config.projectVersion(), permissionConfig.projectVersionUse(), _ -> BuildConstant.PROJECT_VERSION);
        register(config.projectReload(), permissionConfig.projectReloadUse(), this::handlerReload);

        return this;
    }

    @Override
    public ProjectPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(ProjectPlaceholderModule.class);
        permissionRegistry.unregisterAll(ProjectPlaceholderModule.class);

        return this;
    }

    @Override
    public ProjectPlaceholderModule get() {
        return this;
    }

    private String handlerReload(PlaceholderContext context) {
        try {
            ZFuctoneAPI.get().reload();
        } catch (ReloadException e) {
            ZFuctoneAPI.get().logger().warn("Reload failed: {}", e.getMessage());
            return "Failed to reload, See console for details";
        }

        return "Reload complete";
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(ProjectPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(ProjectPlaceholderModule.class, setting.name(), permission.name(), handler);
    }
}
