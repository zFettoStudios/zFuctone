package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderModule;

@UtilityClass
public class ProjectPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();
    private final PlaceholderModule placeholderModule = ZFuctoneAPI.get().moduleController().placeholderModule();

    public void createPlaceholders() {
        placeholderModule.addPlaceholder(config.projectAuthors(),
            permissionConfig.projectAuthorsUse(), _ -> BuildConstant.PROJECT_AUTHORS);
        placeholderModule.addPlaceholder(config.projectDescription(),
            permissionConfig.projectDescriptionUse(), _ -> BuildConstant.PROJECT_DESCRIPTION);
        placeholderModule.addPlaceholder(config.projectWebsite(),
            permissionConfig.projectWebsiteUse(), _ -> BuildConstant.PROJECT_WEBSITE);
        placeholderModule.addPlaceholder(config.projectVersion(),
            permissionConfig.projectVersionUse(), _ -> BuildConstant.PROJECT_VERSION);
        placeholderModule.addPlaceholder(config.projectReload(),
            permissionConfig.projectReloadUse(), ProjectPlaceholderHandler::handlerReload);
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
}
