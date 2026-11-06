package zfuctoneT.util.text;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class StringUtil {
    public static String localeToString(Locale locale) {
        return locale.toLanguageTag().replace('-', '_').toLowerCase();
    }
}
