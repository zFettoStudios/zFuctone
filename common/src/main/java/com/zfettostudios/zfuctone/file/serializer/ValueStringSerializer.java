package com.zfettostudios.zfuctone.file.serializer;

import com.zfettostudios.zfuctone.model.value.ValueString;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.List;

public class ValueStringSerializer extends ValueSerializer<ValueString> {
    @Override
    public void serialize(ValueString value, JsonGenerator generator, SerializationContext context) throws JacksonException {
        if (value == null) {
            generator.writeNull();
            return;
        }

        if (value.isList()) {
            List<String> list = value.getList();
            if (list == null) {
                generator.writeString("");
                return;
            }

            generator.writeStartArray();
            for (String element : list) if (element != null) generator.writeString(element);

            generator.writeEndArray();
            return;
        }

        if (value.isString()) {
            generator.writeString(value.getString());
            return;
        }

        generator.writeNull();
    }
}
