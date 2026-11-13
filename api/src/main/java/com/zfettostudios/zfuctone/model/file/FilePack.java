package com.zfettostudios.zfuctone.model.file;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.annotation.config.LocaleName;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.EnUsLocalization;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.localization.RuRuLocalization;
import com.zfettostudios.zfuctone.reflect.ReflectionResolver;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Accessors(fluent = true)
public class FilePack {
    private final static ReflectionResolver reflectionResolver = ZFuctoneAPI.get().reflectionResolver();

    Config config = new Config();
    CommandConfig command = new CommandConfig();
    IntegrationConfig integration = new IntegrationConfig();
    MessageConfig message = new MessageConfig();
    PermissionConfig permission = new PermissionConfig();
    Map<String, Localization> localizations = new ConcurrentHashMap<>();

    public FilePack() {
        localizations.put(reflectionResolver.getAnnotation(LocaleName.class, RuRuLocalization.class).value(), new RuRuLocalization());
        localizations.put(reflectionResolver.getAnnotation(LocaleName.class, EnUsLocalization.class).value(), new EnUsLocalization());
    }
}
