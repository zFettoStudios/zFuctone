package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface ZFuctone extends Module<ZFuctone> {
    <T> T get(Class<T> type);

    @NonNull <L> L getLoader(Class<L> loader);

    ZFuctone reload() throws ReloadException;
}
