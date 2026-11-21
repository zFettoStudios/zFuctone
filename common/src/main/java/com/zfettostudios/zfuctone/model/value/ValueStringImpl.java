package com.zfettostudios.zfuctone.model.value;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Getter
public class ValueStringImpl implements ValueString {
    private String string;
    private List<String> list;

    public ValueStringImpl() {
    }

    public ValueStringImpl(String string) {
        this.string = string;
    }

    public ValueStringImpl(List<String> list) {
        this.list = list;
    }

    @Override
    public @NonNull String setString(String string) {
        this.string = string;
        this.list = null;

        return string;
    }

    @Override
    public @NonNull List<String> setList(List<String> list) {
        this.list = list;
        this.string = null;

        return list;
    }

    @Override
    public boolean isList() {
        return list != null;
    }

    @Override
    public boolean isString() {
        return string != null;
    }

    @Override
    public @Nullable List<String> getList() {
        return list;
    }

    @Override
    public @Nullable String getString() {
        return string;
    }

    @Override
    public @NonNull List<String> asList() {
        if (list != null) return list;
        return string == null ? List.of() : List.of(string);
    }

    @Override
    public @NonNull ValueString set(@NonNull ValueString valueString) {
        this.string = valueString.getString();
        this.list = valueString.getList();

        return this;
    }

    @Override
    public @NonNull String asString() {
        if (string != null) return string;
        if (list == null) return "";

        StringBuilder sb = new StringBuilder();
        for (String element : list) if (element != null) sb.append(element);

        return sb.toString();
    }
}