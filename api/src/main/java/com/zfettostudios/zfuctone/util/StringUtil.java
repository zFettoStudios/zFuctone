package com.zfettostudios.zfuctone.util;

import lombok.experimental.UtilityClass;

import org.jspecify.annotations.Nullable;

import java.util.Locale;

@UtilityClass
public class StringUtil {
    public String localeToString(@Nullable Locale locale) {
        if (locale == null) return "";
        return locale.toLanguageTag().replace('-', '_').toLowerCase();
    }
}
