package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.util.PlaceholderHandlerUtil;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class ProjectPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();

    public void createPlaceholders(Map<String, Function<PlaceholderContext, String>> placeholders) {
        placeholders.put(config.projectAuthors().name(), ProjectPlaceholderHandler::handlerAuthors);
        placeholders.put(config.projectDescription().name(), ProjectPlaceholderHandler::handlerDescription);
        placeholders.put(config.projectWebsite().name(), ProjectPlaceholderHandler::handlerWebsite);
        placeholders.put(config.projectVersion().name(), ProjectPlaceholderHandler::handlerVersion);
        placeholders.put(config.projectReload().name(), ProjectPlaceholderHandler::handlerReload);
    }

    private String handlerAuthors(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.projectAuthors(), permissionConfig.projectAuthorsUse())) return null;

        return BuildConstant.PROJECT_AUTHORS;
    }

    private String handlerDescription(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.projectDescription(), permissionConfig.projectDescriptionUse())) return null;

        return BuildConstant.PROJECT_DESCRIPTION;
    }

    private String handlerWebsite(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.projectWebsite(), permissionConfig.projectWebsiteUse())) return null;

        return BuildConstant.PROJECT_WEBSITE;
    }

    private String handlerVersion(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.projectVersion(), permissionConfig.projectVersionUse())) return null;

        return BuildConstant.PROJECT_VERSION;
    }

    private String handlerReload(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.projectReload(), permissionConfig.projectReloadUse())) return null;

        try {
            ZFuctoneAPI.get().reload();
        } catch (ReloadException e) {
            ZFuctoneAPI.get().logger().warn("Reload failed: {}", e.getMessage());
            return "Failed to reload, See console for details";
        }

        return "Reload complete";
    }
}
