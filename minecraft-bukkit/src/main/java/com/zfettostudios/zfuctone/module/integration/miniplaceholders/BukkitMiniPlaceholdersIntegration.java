package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.util.StringUtil;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.tag.Tag;

public class BukkitMiniPlaceholdersIntegration implements IntegrationHook<BukkitMiniPlaceholdersIntegration> {
    private IntegrationConfig.MiniPlaceholders config() {
        return ZFuctoneAPI.get().fileFacade().integration().miniplaceholders();
    }

    private final Expansion expansion = createExpansion();

    @Override
    public BukkitMiniPlaceholdersIntegration hook() {
        expansion.register();
        return this;
    }

    @Override
    public BukkitMiniPlaceholdersIntegration unhook() {
        expansion.unregister();
        return this;
    }

    private Expansion createExpansion() {
        return Expansion.builder(config().identifier())
            .author(BuildConstant.PROJECT_NAME)
            .version(BuildConstant.PROJECT_VERSION)
            .audiencePlaceholder("project_authors", (_, _, _) -> Tag.preProcessParsed(BuildConstant.PROJECT_AUTHORS))
            .audiencePlaceholder("project_description", (_, _, _) -> Tag.preProcessParsed(BuildConstant.PROJECT_DESCRIPTION))
            .audiencePlaceholder("project_website", (_, _, _) -> Tag.preProcessParsed(BuildConstant.PROJECT_WEBSITE))
            .audiencePlaceholder("project_version", (_, _, _) -> Tag.preProcessParsed(BuildConstant.PROJECT_VERSION))
            .audiencePlaceholder("player_locale", (player, _, _) ->
                Tag.preProcessParsed(StringUtil.localeToString(player.get(Identity.LOCALE).orElse(null))))
            .build();
    }
}
