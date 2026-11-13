package com.zfettostudios.zfuctone.model.config.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.constant.message.BossBar;
import com.zfettostudios.zfuctone.model.constant.message.Advancement;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class DestinationSetting {
    private Type type;
    private Times times;
    private Long duration;
    private Float health;
    private BossBar.Overlay overlay;
    private BossBar.Color color;
    private Boolean playBoosMusic;
    private Boolean createWorldFog;
    private Boolean darkenScreen;
    private String background;
    private Boolean hasShadow;
    private Long animationTime;
    private Long liveTime;
    private Integer width;
    private Float scale;
    private Integer offsetX;
    private Integer offsetY;
    private Integer offsetZ;
    private String icon;
    private Advancement.Style style;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Times {
        private Long fadeIn;
        private Long stay;
        private Long fadeOut;
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
