package com.zfettostudios.zfuctone.module.placeholder;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.placeholdersapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.module.placeholder.handler.PlayerPlaceholderHandler;
import com.zfettostudios.zfuctone.module.placeholder.handler.ProjectPlaceholderHandler;
import com.zfettostudios.zfuctone.module.placeholder.handler.ServerPlaceholderHandler;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Accessors(fluent = true)
public class PlaceholderModuleImpl implements PlaceholderModule {
    private static final ThreadLocal<Set<String>> RESOLVING = ThreadLocal.withInitial(LinkedHashSet::new);
    private static final Set<String> REPORTED_CYCLES = ConcurrentHashMap.newKeySet();

    @Getter
    private final Map<String, Function<PlaceholderContext, String>> placeholders = new ConcurrentHashMap<>();
    private final List<String> placeholderAPIPlaceholders = new LinkedList<>();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final PlaceholderConfig config = fileFacade.placeholder();
    private final IntegrationConfig.PlaceholderAPI configPlaceholderAPI = fileFacade.integration().placeholderapi();
    private final ZLogger logger = ZFuctoneAPI.get().logger();

    private PlaceholderAPIModule placeholderAPI() {
        return ZFuctoneAPI.get().moduleController().getIntegrationOrNew(PlaceholderAPIModule.class);
    }

    @Override
    public List<String> getPlaceholderAPIPlaceholders() {
        return placeholderAPIPlaceholders;
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

        createPlaceholderAPIPlaceholders();
        createPlaceholders();

        return this;
    }

    private void createPlaceholderAPIPlaceholders() {
        placeholderAPIPlaceholders.clear();

        for (String name : config.values().keySet()) createPlaceholderAPIPlaceholder(name);
        for (Localization localization : fileFacade.localizations().values())
            for (String name : localization.placeholder().values().keySet())
                createPlaceholderAPIPlaceholder(name);

        if (!config.standard().enable()) return;

        createPlaceholderAPIPlaceholder(config.standard().projectAuthors());
        createPlaceholderAPIPlaceholder(config.standard().projectDescription());
        createPlaceholderAPIPlaceholder(config.standard().projectWebsite());
        createPlaceholderAPIPlaceholder(config.standard().projectVersion());
        createPlaceholderAPIPlaceholder(config.standard().projectReload());

        createPlaceholderAPIPlaceholder(config.standard().serverType());
        createPlaceholderAPIPlaceholder(config.standard().serverVersion());
        createPlaceholderAPIPlaceholder(config.standard().serverPlayersOnline());
        createPlaceholderAPIPlaceholder(config.standard().serverPlayersMax());
        createPlaceholderAPIPlaceholder(config.standard().serverTps1());
        createPlaceholderAPIPlaceholder(config.standard().serverTps5());
        createPlaceholderAPIPlaceholder(config.standard().serverTps15());

        createPlaceholderAPIPlaceholder(config.standard().playerNickname());
        createPlaceholderAPIPlaceholder(config.standard().playerUuid());
        createPlaceholderAPIPlaceholder(config.standard().playerIp());
        createPlaceholderAPIPlaceholder(config.standard().playerLocale());
        createPlaceholderAPIPlaceholder(config.standard().playerPing());
        createPlaceholderAPIPlaceholder(config.standard().playerProtocolVersion());
        createPlaceholderAPIPlaceholder(config.standard().playerVersion());
        createPlaceholderAPIPlaceholder(config.standard().playerDisplayVersion());
        createPlaceholderAPIPlaceholder(config.standard().playerEdition());
        createPlaceholderAPIPlaceholder(config.standard().playerDisplayEdition());
        createPlaceholderAPIPlaceholder(config.standard().playerGamemode());
        createPlaceholderAPIPlaceholder(config.standard().playerWorldName());
        createPlaceholderAPIPlaceholder(config.standard().playerWorldEnvironment());
        createPlaceholderAPIPlaceholder(config.standard().playerHasPermission());
        createPlaceholderAPIPlaceholder(config.standard().playerIsOperator());
        createPlaceholderAPIPlaceholder(config.standard().playerIsSneaking());
        createPlaceholderAPIPlaceholder(config.standard().playerIsDead());
    }

    private void createPlaceholderAPIPlaceholder(PlaceholderConfig.Standard.PlaceholderSetting setting) {
        if (!setting.enable()) return;

       createPlaceholderAPIPlaceholder(setting.name());
    }

    private void createPlaceholderAPIPlaceholder(String name) {
        placeholderAPIPlaceholders.add("%" + configPlaceholderAPI.identifier() + "_" + name + "%");
    }

    private void createPlaceholders() {
        placeholders.clear();

        for (Map.Entry<String, String> entry : config.values().entrySet())
            placeholders.put(entry.getKey(), context ->
                placeholderAPI().setPlaceholders(context.getSender(), entry.getValue()));

        Set<String> names = new LinkedHashSet<>();
        for (Localization localization : fileFacade.localizations().values())
            names.addAll(localization.placeholder().values().keySet());

        for (String name : names) {
            placeholders.put(name, context -> {
                Set<String> resolving = RESOLVING.get();

                if (!resolving.add(name)) {
                    if (REPORTED_CYCLES.add(name))
                        logger.warn("Placeholder \"{}\" refers to itself. Check your localization.", name);

                    return "";
                }

                try {
                    PlatformPlayer sender = context.getSender();

                    Localization localization = sender == null
                        ? fileFacade.localization(LocalizationType.PLAYER)
                        : fileFacade.localizationOrDefault(sender.getLocale(), LocalizationType.PLAYER);

                    String text = localization.placeholder().values().get(name);
                    if (text == null) text = fileFacade.localization(LocalizationType.PLAYER).placeholder().values().get(name);

                    return text == null ? null : placeholderAPI().setPlaceholders(sender, text);
                } finally {
                    resolving.remove(name);
                    if (resolving.isEmpty()) RESOLVING.remove();
                }
            });
        }

        if (!config.standard().enable()) return;

        ProjectPlaceholderHandler.createPlaceholders(placeholders);
        ServerPlaceholderHandler.createPlaceholders(placeholders);
        PlayerPlaceholderHandler.createPlaceholders(placeholders);
    }
}