package com.zfettostudios.zfuctone.module.controller;

import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface ModuleController extends Reloadable<ModuleController> {
    <T extends Module<T>> T register(@NonNull Class<T> moduleClass, Module<T> module);
    @Nullable <T extends Module<T>> T unregister(@NonNull Class<T> moduleClass);

    <T extends Module<T>> T get(@NonNull Class<T> moduleClass);

    Map<Class<? extends Module<?>>, Module<?>> modules();
}