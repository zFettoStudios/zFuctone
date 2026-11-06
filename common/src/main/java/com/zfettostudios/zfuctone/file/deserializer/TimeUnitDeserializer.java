package com.zfettostudios.zfuctone.file.deserializer;

import com.zfettostudios.zjtime.TimeUnit;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class TimeUnitDeserializer extends ValueDeserializer<TimeUnit> {
    @Override
    public TimeUnit deserialize(JsonParser parser, DeserializationContext context) {
        return TimeUnit.parse(parser.getString());
    }
}
