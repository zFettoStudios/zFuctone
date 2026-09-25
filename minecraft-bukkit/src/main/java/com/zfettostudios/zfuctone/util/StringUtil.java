package com.zfettostudios.zfuctone.util;

import java.util.Locale;

public class StringUtil {
    public static String join(String... strings) {
        return String.join("", strings);
    }

    public static String localeToString(Locale locale) {
        return locale.toLanguageTag().replace("-", "_").toLowerCase();
    }
}
