package com.zfettostudios.zfuctone.file.deserializer;

import com.zfettostudios.zfuctone.model.value.ValueString;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class ValueStringDeserializer extends ValueDeserializer<ValueString> {
    @Override
    public ValueString deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        JsonToken token = parser.currentToken();

        if (token == JsonToken.VALUE_NULL) return ValueString.build();
        if (token == JsonToken.VALUE_STRING) return ValueString.build(parser.getValueAsString());

        if (token == JsonToken.START_ARRAY) {
            List<String> lines = new ArrayList<>();
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                JsonToken current = parser.currentToken();
                if (current == JsonToken.VALUE_NULL) continue;

                if (current.isStructStart()) {
                    lines.add(context.readTree(parser).toString());
                    continue;
                }

                lines.add(parser.getValueAsString());
            }

            return ValueString.build(lines);
        }

        return (ValueString) context.handleUnexpectedToken(ValueString.class, parser);
    }
}