package com.zfettostudios.zfuctone.model.value;

import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ValueString {
    static @NonNull ValueString build() {
        return FactoryProvider.modelFactory().buildValueString();
    }
    static @NonNull ValueString build(@NonNull String string) {
        return FactoryProvider.modelFactory().buildValueString(string);
    }
    static @NonNull ValueString build(@NonNull List<String> list) {
        return FactoryProvider.modelFactory().buildValueString(list);
    }

    boolean isList();
    boolean isString();


    @Nullable List<String> getList();
    @Nullable String getString();


    @NonNull List<String> setList(List<String> list);
    @NonNull String setString(String string);


    @NonNull String asString();
    @NonNull List<String> asList();

    @NonNull ValueString set(@NonNull ValueString valueString);
}