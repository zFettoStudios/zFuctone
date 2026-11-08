package com.zfettostudios.zfuctone.util;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class ComponentUtil {
    private static final Integration integration = ZFuctoneAPI.get(Integration.class);

    public static Component build(PlatformPlayer sender, String text) {
        return MiniMessage.miniMessage().deserialize(
            integration.placeholderAPI().setPlaceholders(sender, text),
            integration.miniPlaceholders().audienceGlobalPlaceholders(),
            integration.miniPlaceholders().audiencePlaceholders(),
            integration.miniPlaceholders().globalPlaceholders()
        );
    }
}
