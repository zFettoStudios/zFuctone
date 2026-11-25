package com.zfettostudios.zfuctone.model.value;

import org.jspecify.annotations.NonNull;

public interface DestinationValue {
    @NonNull Type type();

    enum Type {
        ACTION_BAR,
        BOSS_BAR,
        BRAND,
        CHAT,
        TITLE,
        SUBTITLE,
        TAB_HEADER,
        TAB_FOOTER,
        TOAST,
        HEAD_TEXT
    }
}
