package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderValueSetting;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CustomPlaceholderModuleImpl implements CustomPlaceholderModule {
    private static final ThreadLocal<Set<String>> RESOLVING = ThreadLocal.withInitial(LinkedHashSet::new);
    private static final Set<String> REPORTED_CYCLES = ConcurrentHashMap.newKeySet();

    private final PlaceholderRegistry placeholderRegistry = ZFuctoneAPI.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneAPI.get().permissionRegistry();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final ZLogger logger = ZFuctoneAPI.get().logger();
    private final PlaceholderAPIModule placeholderAPIModule = ZFuctoneAPI.get().moduleController().get(PlaceholderAPIModule.class);

    private final PlaceholderConfig config = fileFacade.placeholder();
    private final PermissionConfig.Placeholder permissionConfig = fileFacade.permission().placeholder();

    @Override
    public CustomPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public CustomPlaceholderModule enable() {
        if (!config.enable()) return this;

        for (Map.Entry<String, PlaceholderValueSetting> entry : config.values().entrySet()) {
            String placeholderKey = entry.getKey();
            PlaceholderValueSetting valueSetting = entry.getValue();
            if (!valueSetting.enable()) continue;

            PermissionSetting permission = permissionConfig.values().get(placeholderKey);
            register(placeholderKey, permission, createHandler(placeholderKey, valueSetting));
        }

        return this;
    }

    @Override
    public CustomPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(CustomPlaceholderModule.class);
        permissionRegistry.unregisterAll(CustomPlaceholderModule.class);

        return this;
    }

    @Override
    public CustomPlaceholderModule get() {
        return this;
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

    private void register(String placeholderKey, @Nullable PermissionSetting permission,
                          Function<PlaceholderContext, @Nullable String> handler) {
        if (permission != null) {
            permissionRegistry.register(CustomPlaceholderModule.class, permission.name(), permission.type());
            placeholderRegistry.register(CustomPlaceholderModule.class, placeholderKey, permission.name(), handler);
        }
        else placeholderRegistry.register(CustomPlaceholderModule.class, placeholderKey, handler);
    }
}