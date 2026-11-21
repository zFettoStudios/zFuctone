package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;
import java.util.Map;

/**
 * Фасад для работы с файлами.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface FileFacade extends Reloadable<FileFacade> {

    /**
     * Возвращает путь к корневой папке данных проекта.
     *
     * @return путь к папке данных.
     */
    Path dataPath();

    /**
     * Возвращает путь к папке с файлами локализации.
     *
     * @return путь к папке локализаций.
     */
    Path localizationsPath();

    Config config();
    MessageConfig message();
    CommandConfig command();
    PermissionConfig permission();
    IntegrationConfig integration();
    PlaceholderConfig placeholder();

    Map<String, Localization> localizations();

    /**
     * Маппер, через который выполняется загрузка YAML конфигурации.
     *
     * @return маппер.
     */
    YAMLMapper mapper();

    FileLoader loader();
    FileWriter writer();

    /**
     * Получает локализацию по типу по умолчанию.
     *
     * @param type тип локализации.
     * @return локализация или {@code null}, если она не загружена.
     */
    @Nullable Localization localization(@NonNull LocalizationType type);

    /**
     * Получает локализацию по коду языка, если она загружена.
     *
     * @param locale код языка.
     * @return локализация или {@code null}, если не найдена.
     */
    @Nullable Localization localization(@NonNull String locale);

    /**
     * Возвращает локализацию по коду языка либо локализацию по заданному типу.
     *
     * @param locale код языка.
     * @param type тип локализации по умолчанию.
     * @return найденная локализация или локализация по умолчанию.
     */
    Localization localizationOrDefault(@NonNull String locale, @NonNull LocalizationType type);

    /**
     * Сохраняет файлы на диск.
     *
     * @return текущий экземпляр фасада.
     */
    FileFacade saveFiles();

    /**
     * Обновляет кэш-файлов с диска.
     *
     * @return текущий экземпляр фасада.
     */
    FileFacade updateFiles();

    /**
     * Определяет путь к файлу конфигурации на основе {@link FilePath}.
     *
     * @param config объект конфигурации.
     * @param dataPath путь папке данных проекта.
     * @param localizationsPath путь папке локализаций.
     * @return путь к файлу.
     */
    Path getFilePath(@NonNull Object config, @NonNull Path dataPath, @NonNull Path localizationsPath);
}