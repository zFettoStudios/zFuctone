package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderValueSetting;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CustomPlaceholderModuleImpl extends ExtendedModule<CustomPlaceholderModule> implements CustomPlaceholderModule {
    private static final ThreadLocal<Set<String>> RESOLVING = ThreadLocal.withInitial(LinkedHashSet::new);
    private static final Set<String> REPORTED_CYCLES = ConcurrentHashMap.newKeySet();

    private final FileFacade fileFacade = ZFuctoneProvider.get().fileFacade();
    private final ZLogger logger = ZFuctoneProvider.get().logger();
    private final PlaceholderAPIModule placeholderAPIModule = ZFuctoneProvider.get().moduleController().get(PlaceholderAPIModule.class);

    @Override
    protected void onEnable() {
        for (Map.Entry<String, PlaceholderValueSetting> entry : config().values().entrySet()) {
            String placeholderKey = entry.getKey();
            PlaceholderValueSetting valueSetting = entry.getValue();
            if (!valueSetting.enable()) continue;

            PermissionSetting permission = permission().values().get(placeholderKey);
            register(placeholderKey, permission, createHandler(placeholderKey, valueSetting));
        }
    }

    @Override
    public @NonNull CustomPlaceholderModule get() {
        return this;
    }

    @Override
    public @NonNull PlaceholderConfig config() {
        return fileFacade.placeholder();
    }

    @Override
    public PermissionConfig.@NonNull Placeholder permission() {
        return fileFacade.permission().placeholder();
    }

    @Override
    public @NonNull Class<? extends Module<CustomPlaceholderModule>> moduleClass() {
        return CustomPlaceholderModule.class;
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.unregisterAll(moduleClass());
    }

    @Override
    protected void unregisterPlaceholders() {
        placeholderRegistry.unregisterAll(moduleClass());
    }

    private Function<PlaceholderContext, String> createHandler(String placeholderKey, PlaceholderValueSetting valueSetting) {
        return context -> {
            PlatformPlayer sender = context.getSender();
            if (sender == null) return null;

            Set<String> resolving = RESOLVING.get();
            if (!resolving.add(placeholderKey)) {
                if (REPORTED_CYCLES.add(placeholderKey))
                    logger.warn("Placeholder \"{}\" refers to itself. Check your localization.", placeholderKey);

                return null;
            }

            try {
                Localization localization = fileFacade.localizationOrDefault(sender.getLocale(), LocalizationType.PLAYER);
                String rawValue = localization != null && localization.placeholder().values().containsKey(placeholderKey)
                    ? localization.placeholder().values().get(placeholderKey).asString()
                    : valueSetting.value().asString();

                return placeholderAPIModule.setPlaceholders(sender, rawValue);
            } finally {
                resolving.remove(placeholderKey);
                if (resolving.isEmpty()) RESOLVING.remove();
            }
        };
    }

    private void register(String placeholderKey, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (permission != null) {
            permissionRegistry.register(moduleClass(), permission.name(), permission.type());
            placeholderRegistry.register(moduleClass(), placeholderKey, permission.name(), handler);
        }
        else placeholderRegistry.register(moduleClass(), placeholderKey, handler);
    }
}