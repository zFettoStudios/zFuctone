package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.SimpleModule;
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
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CustomPlaceholderModuleImpl extends SimpleModule<CustomPlaceholderModule> implements CustomPlaceholderModule {
    private static final ThreadLocal<Set<String>> RESOLVING = ThreadLocal.withInitial(LinkedHashSet::new);
    private static final Set<String> REPORTED_CYCLES = ConcurrentHashMap.newKeySet();

    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();
    private final FileFacade fileFacade = ZFuctoneProvider.get().fileFacade();
    private final ZLogger logger = ZFuctoneProvider.get().logger();
    private final PlaceholderAPIModule placeholderAPIModule = ZFuctoneProvider.get().moduleController().get(PlaceholderAPIModule.class);

    @Override
    public @NonNull CustomPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    protected @NonNull CustomPlaceholderModule onEnable() {
        if (!config().enable()) return this;

        for (Map.Entry<String, PlaceholderValueSetting> entry : config().values().entrySet()) {
            String placeholderKey = entry.getKey();
            PlaceholderValueSetting valueSetting = entry.getValue();
            if (!valueSetting.enable()) continue;

            PermissionSetting permission = permission().values().get(placeholderKey);
            register(placeholderKey, permission, createHandler(placeholderKey, valueSetting));
        }

        return this;
    }

    @Override
    protected @NonNull CustomPlaceholderModule onDisable() {
        placeholderRegistry.unregisterAll(CustomPlaceholderModule.class);
        permissionRegistry.unregisterAll(CustomPlaceholderModule.class);

        return this;
    }

    @Override
    protected @NonNull CustomPlaceholderModule onReload() {
        onDisable();
        onEnable();

        return this;
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