package com.zfettostudios.zfuctone.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtil {
    public static String join(String[] array) {
        if (array.length == 0) return "";
        else if (array.length == 1) return array[0];

        return String.join("", array);
    }
}
