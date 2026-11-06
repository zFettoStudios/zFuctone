package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.exception.AnnotationNotFoundException;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;
import java.util.Map;

public interface FileFacade extends Module<FileFacade> {
    Path dataPath();
    Path localizationsPath();
    Config config();
    MessageConfig message();
    CommandConfig command();
    PermissionConfig permission();
    IntegrationConfig integration();

    Map<String, Localization> localizations();

    Localization localization(@NonNull LocalizationType type);

    @Nullable Localization localization(@NonNull String locale);

    Localization localizationOrDefault(@NonNull String locale, @NonNull LocalizationType type);

    FileFacade saveFiles();

    FileFacade updateFiles();

    YAMLMapper mapper();

    FileLoader loader();

    FileWriter writer();

    static FilePath getFilePath(Object config) {
        FilePath filePath = config.getClass().getAnnotation(FilePath.class);
        if (filePath == null) throw new AnnotationNotFoundException(config.getClass(), FilePath.class);

        return filePath;
    }

    static FilePath getFilePath(Class<?> config) {
        FilePath filePath = config.getAnnotation(FilePath.class);
        if (filePath == null) throw new AnnotationNotFoundException(config, FilePath.class);

        return filePath;
    }

    static Path getFilePath(Object config, Path dataPath, Path localizationsPath) {
        FilePath filePath = getFilePath(config);
        if (filePath.localization()) return localizationsPath.resolve(filePath.path());

        return dataPath.resolve(filePath.path());
    }
}
