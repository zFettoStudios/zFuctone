package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class ProjectPlaceholderModuleImpl extends StandardPlaceholderModuleImpl<ProjectPlaceholderModule> implements ProjectPlaceholderModule {
    @Override
    public @NonNull ProjectPlaceholderModule get() {
        return this;
    }

    @Override
    public @NonNull Class<? extends Module<ProjectPlaceholderModule>> moduleClass() {
        return ProjectPlaceholderModule.class;
    }

    @Override
    protected void onEnable() {
        register(config().projectAuthors(), permission().projectAuthorsUse(), _ -> BuildConstant.PROJECT_AUTHORS);
        register(config().projectDescription(), permission().projectDescriptionUse(), _ -> BuildConstant.PROJECT_DESCRIPTION);
        register(config().projectWebsite(), permission().projectWebsiteUse(), _ -> BuildConstant.PROJECT_WEBSITE);
        register(config().projectVersion(), permission().projectVersionUse(), _ -> BuildConstant.PROJECT_VERSION);
    }
}
