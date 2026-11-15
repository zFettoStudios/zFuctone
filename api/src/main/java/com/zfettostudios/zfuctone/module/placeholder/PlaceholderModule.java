package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.ReloadModule;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PlaceholderModule extends ReloadModule<PlaceholderModule> {
    Map<String, Function<PlaceholderContext, String>> placeholders();
    List<String> getPlaceholderAPIPlaceholders();
    boolean isEnable();
}
