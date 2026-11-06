package com.zfettostudios.zfuctone.file.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListDeserializer extends ValueDeserializer<List<Object>> {
    private final ValueDeserializer<Object> elementDeserializer;

    public ListDeserializer() {
        this.elementDeserializer = null;
    }

    public ListDeserializer(ValueDeserializer<Object> elementDeserializer) {
        this.elementDeserializer = elementDeserializer;
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
        if (property != null) {
            JavaType elementType = property.getType().containedType(0);
            if (elementType != null) {
                ValueDeserializer<Object> found = context.findNonContextualValueDeserializer(elementType);
                return new ListDeserializer(found);
            }
        }

        return this;
    }

    @Override
    public Boolean supportsUpdate(DeserializationConfig config) {
        return Boolean.TRUE;
    }

    @Override
    public List<Object> getAbsentValue(DeserializationContext context) {
        return null;
    }

    @Override
    public List<Object> getNullValue(DeserializationContext context) {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    public List<Object> deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) return new CopyOnWriteArrayList<>();
        return deserialize(parser, context, new CopyOnWriteArrayList<>());
    }

    @Override
    public List<Object> deserialize(JsonParser parser, DeserializationContext context, List<Object> intoValue) throws JacksonException {
        if (parser.currentToken() == JsonToken.VALUE_NULL) {
            if (intoValue == null) return new CopyOnWriteArrayList<>();

            intoValue.clear();
            return intoValue;
        }

        if (intoValue == null) intoValue = new CopyOnWriteArrayList<>();
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