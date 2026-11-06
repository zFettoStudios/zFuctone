package com.zfettostudios.zfuctone.file.serializer;

import com.zfettostudios.zjtime.TimeUnit;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class TimeUnitSerializer extends ValueSerializer<TimeUnit> {
    @Override
    public void serialize(TimeUnit timeUnit, JsonGenerator generator, SerializationContext context) {
        if (timeUnit == null) generator.writeNull();
        else generator.writeString(timeUnit.getName().toString());
    }
}
