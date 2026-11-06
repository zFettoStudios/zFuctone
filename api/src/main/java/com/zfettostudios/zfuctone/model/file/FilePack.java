package com.zfettostudios.zfuctone.model.file;

import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.EnUsLocalization;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.localization.RuRuLocalization;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Accessors(fluent = true)
public class FilePack {
    Config config = new Config();
    CommandConfig command = new CommandConfig();
    IntegrationConfig integration = new IntegrationConfig();
    MessageConfig message = new MessageConfig();
    PermissionConfig permission = new PermissionConfig();
    Map<String, Localization> localizations = new ConcurrentHashMap<>();

    public FilePack() {
        localizations.put(FileFacade.getFilePath(RuRuLocalization.class).path(), new RuRuLocalization());
        localizations.put(FileFacade.getFilePath(EnUsLocalization.class).path(), new EnUsLocalization());
    }
}
