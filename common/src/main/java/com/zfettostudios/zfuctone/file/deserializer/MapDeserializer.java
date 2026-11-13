package com.zfettostudios.zfuctone.file.deserializer;

import lombok.RequiredArgsConstructor;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.KeyDeserializer;
import tools.jackson.databind.ValueDeserializer;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MapDeserializer extends ValueDeserializer<Map<Object, Object>> {
    private final KeyDeserializer keyDeserializer;
    private final ValueDeserializer<Object> valueDeserializer;

    public MapDeserializer() {
        this(null, null);
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
        if (property != null) {
            JavaType keyType = property.getType().containedType(0);
            JavaType valueType = property.getType().containedType(1);

            KeyDeserializer keyDeserializer = null;
            if (keyType != null) keyDeserializer = context.findKeyDeserializer(keyType, property);

            ValueDeserializer<Object> valueDeserializer = null;
            if (valueType != null) valueDeserializer = context.findNonContextualValueDeserializer(valueType);

            return new MapDeserializer(keyDeserializer, valueDeserializer);
        }
        return this;
    }

    @Override
    public Boolean supportsUpdate(DeserializationConfig config) {
        return Boolean.TRUE;
    }

    @Override
    public Map<Object, Object> getAbsentValue(DeserializationContext context) {
        return null;
    }

    @Override
    public Map<Object, Object> getNullValue(DeserializationContext context) {
        return new LinkedHashMap<>();
    }

    @Override
    public Map<Object, Object> deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) return new LinkedHashMap<>();

        return deserialize(parser, context, new LinkedHashMap<>());
    }

    @Override
    public Map<Object, Object> deserialize(JsonParser parser, DeserializationContext context,
                                           Map<Object, Object> intoValue) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            if (intoValue != null) intoValue.clear();
            return intoValue == null ? new LinkedHashMap<>() : intoValue;
        }

        if (intoValue == null) intoValue = new LinkedHashMap<>();
        else intoValue.clear();

        if (!parser.isExpectedStartObjectToken()) return intoValue;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String rawKey = parser.currentName();
            parser.nextToken();

            Object key = rawKey;
            if (keyDeserializer != null) {
                try {
                    key = keyDeserializer.deserializeKey(rawKey, context);
                } catch (Exception ignored) {}
            }

            if (parser.currentToken() == JsonToken.VALUE_NULL) {
                intoValue.put(key, null);
                continue;
            }

            ValueDeserializer<Object> deserializer = valueDeserializer;
            if (deserializer == null)
                deserializer = context.findNonContextualValueDeserializer(context.constructType(Object.class));

            Object value = deserializer != null ? deserializer.deserialize(parser, context) : parser.getValueAsString();
            intoValue.put(key, value);
        }

        return intoValue;
    }
}