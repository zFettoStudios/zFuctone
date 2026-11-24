package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;
import java.util.Map;

public interface FileFacade extends Reloadable<FileFacade> {
    @NonNull Path dataPath();
    @NonNull Path localizationsPath();

    @NonNull Config config();
    @NonNull MessageConfig message();
    @NonNull CommandConfig command();
    @NonNull PermissionConfig permission();
    @NonNull IntegrationConfig integration();
    @NonNull PlaceholderConfig placeholder();

    @NonNull Map<String, Localization> localizations();

    @NonNull YAMLMapper mapper();

    @NonNull FileLoader loader();
    @NonNull FileWriter writer();

    @Nullable Localization localization(@NonNull LocalizationType type);
    @Nullable Localization localization(@NonNull String locale);

    Localization localizationOrDefault(@NonNull String locale, @NonNull LocalizationType type);

    @NonNull FileFacade saveFiles();

    @NonNull FileFacade updateFiles();

    @NonNull Path getFilePath(@NonNull Object config, @NonNull Path dataPath, @NonNull Path localizationsPath);
}