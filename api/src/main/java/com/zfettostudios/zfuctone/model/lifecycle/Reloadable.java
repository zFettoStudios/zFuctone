package com.zfettostudios.zfuctone.model.lifecycle;

import org.jspecify.annotations.NonNull;

public interface Reloadable<T> extends Toggleable<T> {
    @NonNull T reload();
}
