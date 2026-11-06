package com.zfettostudios.zfuctone.file.handler;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.logging.ZLogger;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.DeserializationProblemHandler;

public class LoggingProblemHandler extends DeserializationProblemHandler {
    private ZLogger logger() {
        return ZFuctoneAPI.get(ZLogger.class);
    }

    @Override
    public Object handleWeirdStringValue(DeserializationContext context, Class<?> type, String value, String failureMessage) {
        logger().warn("Value \"{}\" is not valid for \"{}\", fallback", value, type.getSimpleName());
        return fallback(type);
    }

    @Override
    public Object handleWeirdNumberValue(DeserializationContext context, Class<?> type, Number value, String failureMessage) {
        logger().warn("Value \"{}\" is not valid for \"{}\", fallback", value, type.getSimpleName());
        return fallback(type);
    }

    @Override
    public Object handleUnexpectedToken(DeserializationContext context, JavaType type, JsonToken token, JsonParser parser, String failureMessage) {
        logger().warn("Expected {} but found \"{}\", skipping", type, token);
        parser.skipChildren();
        return fallback(type.getRawClass());
    }

    @Override
    public boolean handleUnknownProperty(DeserializationContext context, JsonParser parser, ValueDeserializer<?> deserializer, Object target, String property) {
        logger().warn("Unknown key \"{}\", ignoring", property);
        parser.skipChildren();
        return true;
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
}
