package zfuctoneT.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import zfuctoneT.ZFuctone;
import zfuctoneT.annotation.config.FilePath;
import zfuctoneT.model.config.CommandConfig;
import zfuctoneT.model.config.Config;
import zfuctoneT.model.config.IntegrationConfig;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.config.MessageConfig;
import zfuctoneT.model.config.PermissionConfig;
import zfuctoneT.model.config.data.PlayerConfig;
import zfuctoneT.model.config.data.SpawnConfig;
import zfuctoneT.model.Module;
import zfuctoneT.util.time.CustomTimeUnits;
import com.zfettostudios.zjtime.TimeUnit;
import lombok.Getter;
import lombok.Locked;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.snakeyaml.engine.v2.api.LoadSettings;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.deser.DeserializationProblemHandler;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Accessors(fluent = true)
public class FileFacade implements Module<FileFacade> {
    Map<String, Localization> localizations = new ConcurrentHashMap<>();

    Config config = new Config();
    CommandConfig command = new CommandConfig();
    PermissionConfig permission = new PermissionConfig();
    IntegrationConfig integration = new IntegrationConfig();
    MessageConfig message = new MessageConfig();
    Data data = new Data();

    Path dataPath;
    Path localizationsPath;
    Path playersPath;

    YAMLMapper mapper = createMapper();
    FileLoader fileLoader = new FileLoader();
    FileWriter fileWriter = new FileWriter();

    public FileFacade(Path dataPath) {
        this.dataPath = dataPath;
        localizationsPath = dataPath.resolve("localizations");
        playersPath = dataPath.resolve("data").resolve("players");
    }

    @Override
    public FileFacade enable() {
        return loadAll();
    }

    @Override
    public FileFacade disable() {
        return saveAll();
    }

    @Override
    public FileFacade reload() {
        return loadAll().saveAll();
    }

    private YAMLMapper createMapper() {
        return YAMLMapper.builder(YAMLFactory.builder().loadSettings(LoadSettings.builder()
                .setBufferSize(8192)
                .setAllowDuplicateKeys(true)
                .build()
            ).build())
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .disable(MapperFeature.DETECT_PARAMETER_NAMES)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(YAMLWriteFeature.SPLIT_LINES)
            .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
            .disable(YAMLWriteFeature.USE_NATIVE_TYPE_ID)
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
            .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
            .changeDefaultPropertyInclusion(_ -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .changeDefaultNullHandling(_ -> JsonSetter.Value.forValueNulls(Nulls.SKIP))
            .withConfigOverride(String.class, o -> o.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Collection.class, o -> o.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(List.class, o -> o.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Set.class, o -> o.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .withConfigOverride(Map.class, o -> o.setNullHandling(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY)))
            .addHandler(new DeserializationProblemHandler() {
                @Override
                public Object handleWeirdStringValue(DeserializationContext context, Class<?> type, String value, String failureMessage) {
                    ZFuctone.get(Logger.class).warn("Value \"{}\" is not valid for \"{}\", fallback", value, type.getSimpleName());
                    return fallback(type);
                }

                @Override
                public Object handleWeirdNumberValue(DeserializationContext context, Class<?> type, Number value, String failureMessage) {
                    ZFuctone.get(Logger.class).warn("Value \"{}\" is not valid for \"{}\", fallback", value, type.getSimpleName());
                    return fallback(type);
                }

                @Override
                public Object handleUnexpectedToken(DeserializationContext context, JavaType type, JsonToken token, JsonParser parser, String failureMessage) {
                    ZFuctone.get(Logger.class).warn("Expected {} but found \"{}\", skipping", type, token);
                    parser.skipChildren();
                    return fallback(type.getRawClass());
                }

                @Override
                public boolean handleUnknownProperty(DeserializationContext context, JsonParser parser, ValueDeserializer<?> deserializer, Object target, String property) {
                    ZFuctone.get(Logger.class).warn("Unknown key \"{}\", ignoring", property);
                    parser.skipChildren();
                    return true;
                }
            })
            .addModule(new SimpleModule()
                .addDeserializer(String.class, new ValueDeserializer<>() {
                    @Override
                    public String deserialize(JsonParser parser, DeserializationContext context) {
                        return parser.currentToken() == JsonToken.VALUE_NULL ? "" : parser.getString();
                    }
                    @Override public String getNullValue(DeserializationContext context) { return ""; }
                    @Override public String getAbsentValue(DeserializationContext context) { return null; }
                })
                .addDeserializer(Boolean.class, new ValueDeserializer<>() {
                    @Override
                    public Boolean deserialize(JsonParser parser, DeserializationContext context) {
                        if (parser.currentToken() == JsonToken.VALUE_TRUE) return Boolean.TRUE;
                        if (parser.currentToken() == JsonToken.VALUE_FALSE) return Boolean.FALSE;
                        if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) return parser.getIntValue() != 0;

                        String value = String.valueOf(parser.getString()).trim().toLowerCase();
                        return switch (value) {
                            case "true", "yes", "y", "on", "enable", "enabled" -> Boolean.TRUE;
                            case "false", "no", "n", "off", "disable", "disabled" -> Boolean.FALSE;
                            default -> {
                                ZFuctone.get(Logger.class).warn("Value \"{}\" is not boolean", value);
                                yield null;
                            }
                        };
                    }
                })
                .addDeserializer(TimeUnit.class, new ValueDeserializer<>() {
                    @Override
                    public TimeUnit deserialize(JsonParser parse, DeserializationContext context) {
                        return CustomTimeUnits.parse(parse.getString());
                    }
                })
                .addSerializer(TimeUnit.class, new ValueSerializer<>() {
                    @Override
                    public void serialize(TimeUnit timeUnit, JsonGenerator generator, SerializationContext context) {
                        if (timeUnit == null) generator.writeNull();
                        else generator.writeString(timeUnit.getName().toString());
                    }
                })
            )
            .build();
    }

    private Object fallback(Class<?> type) {
        if (!type.isPrimitive()) return null;
        if (type == boolean.class) return Boolean.FALSE;
        if (type == long.class) return 0L;
        if (type == double.class) return 0.0d;
        if (type == float.class) return 0.0f;
        if (type == short.class) return (short) 0;
        if (type == byte.class) return (byte) 0;
        if (type == char.class) return (char) 0;
        return 0;
    }

    private FileFacade saveAll() {
        return save(config)
            .save(command)
            .save(permission)
            .save(integration)
            .save(message)
            .save(data.spawn)
            .saveLocalizations()
            .savePlayers();
    }

    private FileFacade savePlayers() {
        data.players.values().forEach(player -> save(player, playersPath.resolve(player.uuid() + ".yml").toString()));
        return this;
    }

    private FileFacade saveLocalizations() {
        localizations.forEach((key, value) -> save(value, localizationsPath.resolve(key + ".yml").toString()));
        return this;
    }

    private FileFacade loadAll() {
        load(Config.class);
        load(CommandConfig.class);
        load(PermissionConfig.class);
        load(IntegrationConfig.class);
        load(MessageConfig.class);
        load(SpawnConfig.class);

        return loadLocalizations().loadPlayers();
    }

    private FileFacade loadLocalizations() {
        File dir = localizationsPath.toFile();
        if (!dir.exists()) dir.mkdirs();
        if (!dir.isDirectory()) return this;

        File[] files = dir.listFiles((_, name) -> name.regionMatches(true, name.length() - 4, ".yml", 0, 4));
        if (files == null || files.length == 0) return this;

        Arrays.stream(files).forEach(file ->
            Optional.ofNullable(load(Localization.class, localizationsPath.resolve(file.getName()).toString()))
                .ifPresent(localization -> localizations.put(getFileName(file).toLowerCase(), localization))
        );
        return this;
    }

    private FileFacade loadPlayers() {
        File dir = playersPath.toFile();
        if (!dir.exists()) dir.mkdirs();
        if (!dir.isDirectory()) return this;

        File[] files = dir.listFiles((_, name) -> name.regionMatches(true, name.length() - 4, ".yml", 0, 4));
        if (files == null || files.length == 0) return this;

        Arrays.stream(files).forEach(file ->
            Optional.ofNullable(load(PlayerConfig.class, playersPath.resolve(file.getName()).toString()))
                .ifPresent(player -> data.players.put(UUID.fromString(getFileName(file)), player))
        );
        return this;
    }

    private String getFileName(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot <= 0) return name;
        return name.substring(0, lastDot);
    }

    @Locked.Write
    public <T> T load(@NonNull Class<T> target) {
        return load(target, getFilePath(target));
    }

    @Locked.Write
    public <T> T load(@NonNull Class<T> target, @NonNull String relativePath) {
        return fileLoader.load(target, relativePath, getExistingConfigInstance(target));
    }

    private <T> T getExistingConfigInstance(Class<T> target) {
        if (target == Config.class) return target.cast(config);
        if (target == CommandConfig.class) return target.cast(permission);
        if (target == PermissionConfig.class) return target.cast(permission);
        if (target == IntegrationConfig.class) return target.cast(integration);
        if (target == MessageConfig.class) return target.cast(message);
        if (target == SpawnConfig.class) return target.cast(data.spawn);

        return null;
    }

    @Locked.Write
    public FileFacade save(@NonNull Object config) {
        return save(config, getFilePath(config.getClass()));
    }

    @Locked.Write
    public FileFacade save(@NonNull Object config, @NonNull String relativePath) {
        fileWriter.save(config, relativePath);
        return this;
    }

    private String getFilePath(Class<?> target) {
        FilePath annotation = target.getAnnotation(FilePath.class);
        if (annotation == null) throw new IllegalArgumentException("Класс " + target.getSimpleName() + " не имеет аннотации @FilePath!");
        return annotation.value();
    }

    @Setter
    @Getter
    public static class Data {
        SpawnConfig spawn = new SpawnConfig();
        @NonFinal Map<UUID, PlayerConfig> players = new ConcurrentHashMap<>();
    }
}