package com.zfettostudios.zfuctone.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.deserializer.BooleanDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.ListDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.MapDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.SetDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.StringDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.TimeUnitDeserializer;
import com.zfettostudios.zfuctone.file.handler.LoggingProblemHandler;
import com.zfettostudios.zfuctone.file.serializer.TimeUnitSerializer;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.file.FilePack;
import com.zfettostudios.zjtime.TimeUnit;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.snakeyaml.engine.v2.api.LoadSettings;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Accessors(fluent = true)
public class FileFacadeImpl implements FileFacade {
    Path dataPath;
    Path localizationsPath;

    FileLoader loader;
    FileWriter writer;
    YAMLMapper mapper = createMapper();
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Getter(AccessLevel.NONE) FilePack files = new FilePack();

    @NonFinal Localization localizationPlayerDefault;
    @NonFinal Localization localizationConsole;
    @NonFinal Localization localizationCommandBlockDefault;

    public FileFacadeImpl(Path dataPath) {
        this.dataPath = dataPath;
        localizationsPath = dataPath.resolve("localizations");

        loader = new FileLoaderImpl(dataPath, localizationsPath, mapper, executor);
        writer = new FileWriterImpl(dataPath, localizationsPath, mapper, executor);
    }

    private ZLogger logger() {
        return ZFuctoneAPI.get().logger();
    }

    @Override
    public Config config() {
        return files.config();
    }

    @Override
    public MessageConfig message() {
        return files.message();
    }

    @Override
    public CommandConfig command() {
        return files.command();
    }

    @Override
    public PermissionConfig permission() {
        return files.permission();
    }

    @Override
    public IntegrationConfig integration() {
        return files.integration();
    }

    @Override
    public Map<String, Localization> localizations() {
        return files.localizations();
    }

    @Override
    public Localization localization(@NonNull LocalizationType type) {
        return switch (type) {
            case PLAYER -> localizationPlayerDefault;
            case CONSOLE -> localizationConsole;
            case COMMAND_BLOCK -> localizationCommandBlockDefault;
        };
    }

    @Override
    public @Nullable Localization localization(@NonNull String locale) {
        return localizations().get(locale);
    }

    @Override
    public Localization localizationOrDefault(@NonNull String locale, @NonNull LocalizationType type) {
        switch (type) {
            case PLAYER -> {
                if (!config().language().player().byPlayer()) return localizationPlayerDefault;
            }
            case CONSOLE -> {
                return localizationConsole;
            }
            case COMMAND_BLOCK -> {
                if (config().language().commandBlock().byConsole()) return localizationConsole;
                else if (!config().language().commandBlock().byPlayer()) return localizationCommandBlockDefault;
            }
        }

        Localization localization = localization(locale);
        if (localization == null) return localizationPlayerDefault;

        return localization;
    }

    @Override
    public FileFacade saveFiles() {
        writer.saveAsync(files).join();
        return this;
    }

    @Override
    public FileFacade updateFiles() {
        loader.loadAsync(files).join();
        saveFiles();

        loader.loadAsync(files).join();

        localizationPlayerDefault = localization(config().language().player().type());
        localizationConsole = localization(config().language().console().type());
        localizationCommandBlockDefault = localization(config().language().commandBlock().type());

        return this;
    }

    @Override
    public FileFacade enable() {
        return updateFiles();
    }

    @Override
    public FileFacade disable() {
        return saveFiles();
    }

    private YAMLMapper createMapper() {
        return YAMLMapper.builder(YAMLFactory.builder().loadSettings(LoadSettings.builder()
                .setBufferSize(8192)
                .setAllowDuplicateKeys(true)
                .build()
            ).build())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
            .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
            .disable(YAMLWriteFeature.SPLIT_LINES)
            .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
            .disable(YAMLWriteFeature.USE_NATIVE_TYPE_ID)
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .disable(MapperFeature.DETECT_PARAMETER_NAMES)
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .changeDefaultPropertyInclusion(_ -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .changeDefaultNullHandling(_ -> JsonSetter.Value.forValueNulls(Nulls.SKIP))
            .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY))
            .withConfigOverride(Object.class, override -> override.setMergeable(true))
            .withConfigOverride(String.class, override -> override.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Collection.class, override -> override.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(List.class, override -> override.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Set.class, override -> override.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Map.class, override -> override.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .addHandler(new LoggingProblemHandler())
            .addModule(new SimpleModule()
                .addDeserializer(String.class, new StringDeserializer())
                .addDeserializer(Boolean.class, new BooleanDeserializer())
                .addDeserializer(TimeUnit.class, new TimeUnitDeserializer())
                .addDeserializer(List.class, new ListDeserializer())
                .addDeserializer(Set.class, new SetDeserializer())
                .addDeserializer(Map.class, new MapDeserializer())

                .addSerializer(TimeUnit.class, new TimeUnitSerializer())
            )
            .build();
    }
}
