package com.zfettostudios.zfuctone.file.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetDeserializer extends ValueDeserializer<Set<Object>> {
    private final ValueDeserializer<Object> elementDeserializer;

    public SetDeserializer() {
        this.elementDeserializer = null;
    }

    public SetDeserializer(ValueDeserializer<Object> elementDeserializer) {
        this.elementDeserializer = elementDeserializer;
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
        if (property != null) {
            JavaType elementType = property.getType().containedType(0);
            if (elementType != null) {
                ValueDeserializer<Object> found = context.findNonContextualValueDeserializer(elementType);
                return new SetDeserializer(found);
            }
        }

        return this;
    }

    @Override
    public Boolean supportsUpdate(DeserializationConfig config) {
        return Boolean.TRUE;
    }

    @Override
    public Set<Object> getAbsentValue(DeserializationContext context) {
        return null;
    }

    @Override
    public Set<Object> getNullValue(DeserializationContext context) {
        return new CopyOnWriteArraySet<>();
    }

    @Override
    public Set<Object> deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) return new CopyOnWriteArraySet<>();
        return deserialize(parser, context, new CopyOnWriteArraySet<>());
    }

    @Override
    public Set<Object> deserialize(JsonParser parser, DeserializationContext context, Set<Object> intoValue) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            if (intoValue != null) intoValue.clear();
            return intoValue == null ? new CopyOnWriteArraySet<>() : intoValue;
        }

        if (intoValue == null) intoValue = new CopyOnWriteArraySet<>();
        else intoValue.clear();

        if (!parser.isExpectedStartArrayToken()) {
            Object value = elementDeserializer != null ? elementDeserializer.deserialize(parser, context) : parser.getValueAsString();
            if (value != null) intoValue.add(value);
            return intoValue;
        }

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.currentToken() == JsonToken.VALUE_NULL) continue;

            Object value = elementDeserializer != null ? elementDeserializer.deserialize(parser, context) : parser.getValueAsString();
            if (value != null) intoValue.add(value);
        }

        return intoValue;
    }
}
