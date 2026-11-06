package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import com.zfettostudios.zfuctone.model.constant.message.BossBar;
import com.zfettostudios.zfuctone.model.constant.message.Advancement;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(makeFinal = false)
public class DestinationSetting {
    Type type;
    Times times;
    Long duration;
    Float health;
    BossBar.Overlay overlay;
    BossBar.Color color;
    Boolean playBoosMusic;
    Boolean createWorldFog;
    Boolean darkenScreen;
    String background;
    Boolean hasShadow;
    Long animationTime;
    Long liveTime;
    Integer width;
    Float scale;
    Integer offsetX;
    Integer offsetY;
    Integer offsetZ;
    String icon;
    Advancement.Style style;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(makeFinal = false)
    public static class Times {
        Long fadeIn;
        Long stay;
        Long fadeOut;
    }

    public enum Type {
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
