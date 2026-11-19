package com.zfettostudios.zfuctone.module.placeholder;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderValueSetting;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.placeholder.handler.PlayerPlaceholderHandler;
import com.zfettostudios.zfuctone.module.placeholder.handler.ProjectPlaceholderHandler;
import com.zfettostudios.zfuctone.module.placeholder.handler.ServerPlaceholderHandler;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PlaceholderModuleImpl implements PlaceholderModule {
    private static final ThreadLocal<Set<String>> RESOLVING = ThreadLocal.withInitial(LinkedHashSet::new);
    private static final Set<String> REPORTED_CYCLES = ConcurrentHashMap.newKeySet();

    @Getter
    private final Map<String, Function<PlaceholderContextImpl, String>> placeholders = new ConcurrentHashMap<>();
    private final List<String> placeholderAPIPlaceholders = new LinkedList<>();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final PlaceholderConfig config = fileFacade.placeholder();
    private final IntegrationConfig.PlaceholderAPI configPlaceholderAPI = fileFacade.integration().placeholderapi();
    private final PermissionConfig.Placeholder permissionConfig = fileFacade.permission().placeholder();
    private final ZLogger logger = ZFuctoneAPI.get().logger();

    private PlaceholderAPIModule placeholderAPI() {
        return ZFuctoneAPI.get().moduleController().getIntegration(PlaceholderAPIModule.class);
    }

    @Override
    public boolean isEnable() {
        return config.enable();
    }

    @Override
    public PlaceholderModule enable() {
        return reload();
    }

    @Override
    public PlaceholderModule disable() {
        return this;
    }

    @Override
    public PlaceholderModule reload() {
        if (!isEnable()) return this;

        createPlaceholders();

        return this;
    }

    @Override
    public PlaceholderModule addPlaceholder(@NonNull PlaceholderSetting config,
                                            @NonNull PermissionSetting permission,
                                            @NonNull Function<PlaceholderContextImpl, String> handler) {
        return addPlaceholder(config, permission, handler, true);
    }

    @Override
    public PlaceholderModule addPlaceholder(@NonNull PlaceholderSetting config,
                                            @NonNull Function<PlaceholderContextImpl, String> handler) {
        return addPlaceholder(config, null, handler, false);
    }

    private PlaceholderModule addPlaceholder(@NonNull PlaceholderSetting config,
                                             PermissionSetting permission,
                                             @NonNull Function<PlaceholderContextImpl, String> handler,
                                             boolean shouldCheckPermission) {
        if (!config.enable()) return this;

        createPlaceholderAPIPlaceholder(config.name());

        if (shouldCheckPermission) {
            placeholders.put(config.name(), context -> {
                PlatformPlayer sender = context.getSender();
                if (sender == null || !sender.hasPermission(permission.name())) return null;

                return handler.apply(context);
            });
        }
        else placeholders.put(config.name(), handler);

        return this;
    }

    private void addPlaceholder(String name,
                                boolean enable,
                                PermissionSetting permission,
                                Function<PlaceholderContextImpl, String> handler,
                                boolean shouldCheckPermission) {
        if (!enable) return;

        createPlaceholderAPIPlaceholder(name);

        if (shouldCheckPermission) {
            placeholders.put(name, context -> {
                PlatformPlayer sender = context.getSender();
                if (sender == null || !sender.hasPermission(permission.name())) return null;

                return handler.apply(context);
            });
        }
        else placeholders.put(name, handler);

    }

    @Override
    public @NonNull List<String> getPlaceholderAPIPlaceholders() {
        return placeholderAPIPlaceholders;
    }

    private void createPlaceholderAPIPlaceholder(String name) {
        placeholderAPIPlaceholders.add("%" + configPlaceholderAPI.identifier() + "_" + name + "%");
    }

    private void createPlaceholders() {
        placeholders.clear();
        placeholderAPIPlaceholders.clear();

        for (Map.Entry<String, PlaceholderValueSetting> entry : config.values().entrySet()) {
            String placeholderKey = entry.getKey();

            Function<PlaceholderContextImpl, String> handler = context -> {
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
                    if (localization == null) return placeholderAPI().setPlaceholders(sender, entry.getValue().value());

                    String value = localization.placeholder().values().get(placeholderKey);
                    if (value == null) return placeholderAPI().setPlaceholders(sender, entry.getValue().value());

                    return placeholderAPI().setPlaceholders(sender, value);
                } finally {
                    resolving.remove(placeholderKey);
                    if (resolving.isEmpty()) RESOLVING.remove();
                }
            };

            PermissionSetting permission = permissionConfig.values().get(placeholderKey);
            addPlaceholder(placeholderKey, entry.getValue().enable(),
                permission, handler, permission != null);
        }

        if (!config.standard().enable()) return;

        ProjectPlaceholderHandler.createPlaceholders();
        ServerPlaceholderHandler.createPlaceholders();
        PlayerPlaceholderHandler.createPlaceholders();
    }
}