package com.zfettostudios.zfuctone.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.file.deserializer.BooleanDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.ListDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.MapDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.SetDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.StringDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.TimeUnitDeserializer;
import com.zfettostudios.zfuctone.file.deserializer.ValueStringDeserializer;
import com.zfettostudios.zfuctone.file.handler.LoggingProblemHandler;
import com.zfettostudios.zfuctone.file.serializer.TimeUnitSerializer;
import com.zfettostudios.zfuctone.file.serializer.ValueStringSerializer;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.model.file.FilePack;
import com.zfettostudios.zfuctone.model.value.ValueString;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Accessors(fluent = true)
public class FileFacadeImpl implements FileFacade {
    private static final String PACKAGE_PATH = Config.class.getPackage().getName();

    private final Path dataPath;
    private final Path localizationsPath;

    private final FileLoader loader;
    private final FileWriter writer;
    private final YAMLMapper mapper = createMapper();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final ReflectionResolver reflectionResolver = ZFuctoneProvider.get().reflectionResolver();

    @Getter(AccessLevel.NONE)
    private final FilePack files = new FilePack();

    private Localization localizationPlayerDefault;
    private Localization localizationConsole;
    private Localization localizationCommandBlockDefault;

    public FileFacadeImpl(Path dataPath) {
        this.dataPath = dataPath;
        localizationsPath = dataPath.resolve("localizations");

        loader = new FileLoaderImpl(this, dataPath, localizationsPath, mapper, executor);
        writer = new FileWriterImpl(this, dataPath, localizationsPath, mapper, executor);
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
    public PlaceholderConfig placeholder() {
        return files.placeholder();
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

        localizationPlayerDefault = localization(config().language().player().type());
        localizationConsole = localization(config().language().console().type());
        localizationCommandBlockDefault = localization(config().language().commandBlock().type());

        saveFiles();

        return this;
    }

    @Override
    public Path getFilePath(@NonNull Object config, @NonNull Path dataPath, @NonNull Path localizationsPath) {
        FilePath filePath = reflectionResolver.getAnnotation(FilePath.class, config);
        if (filePath.localization()) return localizationsPath.resolve(filePath.path());

        return dataPath.resolve(filePath.path());
    }

    @Override
    public FileFacade enable() {
        return updateFiles();
    }

    @Override
    public FileFacade disable() {
        return saveFiles();
    }

    @Override
    public FileFacade reload() {
        return updateFiles();
    }

    private YAMLMapper createMapper() {
        YAMLMapper.Builder builder = YAMLMapper.builder(YAMLFactory.builder().loadSettings(LoadSettings.builder()
                .setBufferSize(8192)
                .setAllowDuplicateKeys(true)
                .build()
            ).build())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
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
                .addDeserializer(ValueString.class, new ValueStringDeserializer())

                .addSerializer(TimeUnit.class, new TimeUnitSerializer())
                .addSerializer(ValueString.class, new ValueStringSerializer())
            );

        Set<Class<?>> visited = new HashSet<>();
        List.of(
            Config.class,
            CommandConfig.class,
            IntegrationConfig.class,
            MessageConfig.class,
            PermissionConfig.class,
            PlaceholderConfig.class,
            Localization.class
        ).forEach(root -> enableMerge(builder, root, visited));

        return builder.build();
    }

    private static void enableMerge(YAMLMapper.Builder builder, Class<?> type, Set<Class<?>> visited) {
        if (type == null || !type.getName().startsWith(PACKAGE_PATH) || !visited.add(type)) return;

        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;

            Class<?> fieldType = field.getType();
            if (fieldType.isEnum() || !fieldType.getName().startsWith(PACKAGE_PATH)) continue;

            builder.withConfigOverride(fieldType, override -> override.setMergeable(true));
            enableMerge(builder, fieldType, visited);
        }

        enableMerge(builder, type.getSuperclass(), visited);
    }
}
