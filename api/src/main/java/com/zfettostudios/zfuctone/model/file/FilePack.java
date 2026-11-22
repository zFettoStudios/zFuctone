package com.zfettostudios.zfuctone.model.file;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.annotation.config.LocaleName;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.EnUsLocalization;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.localization.RuRuLocalization;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Accessors(fluent = true)
public class FilePack {
    private final static ReflectionResolver reflectionResolver = ZFuctoneProvider.get().reflectionResolver();

    private final Config config = new Config();
    private final CommandConfig command = new CommandConfig();
    private final IntegrationConfig integration = new IntegrationConfig();
    private final MessageConfig message = new MessageConfig();
    private final PermissionConfig permission = new PermissionConfig();
    private final PlaceholderConfig placeholder = new PlaceholderConfig();
    private final Map<String, Localization> localizations = new ConcurrentHashMap<>();

    public FilePack() {
        localizations.put(reflectionResolver.getAnnotation(LocaleName.class, RuRuLocalization.class).value(), new RuRuLocalization());
        localizations.put(reflectionResolver.getAnnotation(LocaleName.class, EnUsLocalization.class).value(), new EnUsLocalization());
    }
}
