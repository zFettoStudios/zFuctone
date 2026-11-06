package com.zfettostudios.zfuctone.file.deserializer;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class StringDeserializer extends ValueDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) {
        return parser.currentToken() == JsonToken.VALUE_NULL ? "" : parser.getString();
    }
    @Override public String getNullValue(DeserializationContext context) {
        return "";
    }
    @Override public String getAbsentValue(DeserializationContext context) {
        return null;
    }
}
