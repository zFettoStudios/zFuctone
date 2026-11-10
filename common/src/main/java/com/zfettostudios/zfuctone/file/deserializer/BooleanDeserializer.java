package com.zfettostudios.zfuctone.file.deserializer;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.logging.ZLogger;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class BooleanDeserializer extends ValueDeserializer<Boolean> {
    private ZLogger logger() {
        return ZFuctoneAPI.get().logger();
    }

    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext context) {
        JsonToken token = parser.currentToken();

        if (token == JsonToken.VALUE_TRUE) return Boolean.TRUE;
        if (token == JsonToken.VALUE_FALSE) return Boolean.FALSE;

        if (token == JsonToken.VALUE_NUMBER_INT) return parser.getLongValue() != 0;

        if (token == JsonToken.VALUE_STRING) {
            String text = parser.getString();
            if (text == null) return null;

            String value = text.trim().toLowerCase();
            return switch (value) {
                case "true", "yes", "y", "on", "enable", "enabled" -> Boolean.TRUE;
                case "false", "no", "n", "off", "disable", "disabled" -> Boolean.FALSE;
                default -> {
                    logger().warn("Value \"{}\" is not boolean", value);
                    yield null;
                }
            };
        }

        logger().warn("Token \"{}\" is not boolean", token);
        return null;
    }

}
