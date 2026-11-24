package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class ProjectPlaceholderModuleImpl extends StandardPlaceholderModuleImpl<ProjectPlaceholderModule> implements ProjectPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    @Override
    public @NonNull ProjectPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    protected @NonNull ProjectPlaceholderModule onEnable() {
        if (!config().enable()) return this;

        register(config().projectAuthors(), permission().projectAuthorsUse(), _ -> BuildConstant.PROJECT_AUTHORS);
        register(config().projectDescription(), permission().projectDescriptionUse(), _ -> BuildConstant.PROJECT_DESCRIPTION);
        register(config().projectWebsite(), permission().projectWebsiteUse(), _ -> BuildConstant.PROJECT_WEBSITE);
        register(config().projectVersion(), permission().projectVersionUse(), _ -> BuildConstant.PROJECT_VERSION);

        return this;
    }

    @Override
    protected @NonNull ProjectPlaceholderModule onDisable() {
        placeholderRegistry.unregisterAll(ProjectPlaceholderModule.class);
        permissionRegistry.unregisterAll(ProjectPlaceholderModule.class);

        return this;
    }

    @Override
    protected @NonNull ProjectPlaceholderModule onReload() {
        onDisable();
        onEnable();

        return this;
    }

    @Override
    public @NonNull ProjectPlaceholderModule get() {
        return this;
    }

    @Override
    public @NonNull Class<? extends Module<ProjectPlaceholderModule>> moduleClass() {
        return ProjectPlaceholderModule.class;
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(ProjectPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(ProjectPlaceholderModule.class, setting.name(), permission.name(), handler);
    }
}
