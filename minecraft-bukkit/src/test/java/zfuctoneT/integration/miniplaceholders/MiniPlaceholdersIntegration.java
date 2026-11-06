package zfuctoneT.integration.miniplaceholders;

import zfuctoneT.ZFuctone;
import com.zfettostudios.zfuctone.config.BuildConfig;
import zfuctoneT.integration.IntegrationHook;
import zfuctoneT.model.config.IntegrationConfig;
import zfuctoneT.util.PlayerUtil;
import zfuctoneT.util.text.StringUtil;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.minimessage.tag.Tag;

public class MiniPlaceholdersIntegration implements IntegrationHook<MiniPlaceholdersIntegration> {
    private final static IntegrationConfig.MiniPlaceholders config = ZFuctone.get(IntegrationConfig.class).miniplaceholders();

    private final Expansion expansion = createExpansion();

    @Override
    public MiniPlaceholdersIntegration hook() {
        expansion.register();
        return this;
    }

    @Override
    public MiniPlaceholdersIntegration unhook() {
        expansion.unregister();
        return this;
    }

    private Expansion createExpansion() {
        return Expansion.builder(config.identifier())
            .author(BuildConfig.PROJECT_NAME)
            .version(BuildConfig.PROJECT_VERSION)
            .audiencePlaceholder("project_authors", (_, _, _) -> Tag.preProcessParsed(BuildConfig.PROJECT_AUTHORS))
            .audiencePlaceholder("project_description", (_, _, _) -> Tag.preProcessParsed(BuildConfig.PROJECT_DESCRIPTION))
            .audiencePlaceholder("project_website", (_, _, _) -> Tag.preProcessParsed(BuildConfig.PROJECT_WEBSITE))
            .audiencePlaceholder("project_version", (_, _, _) -> Tag.preProcessParsed(BuildConfig.PROJECT_VERSION))
            .audiencePlaceholder("player_locale", (player, _, _) ->
                Tag.preProcessParsed(StringUtil.localeToString(PlayerUtil.getLocale(player))))
            .audiencePlaceholder("player_version", (player, _, _) ->
                Tag.preProcessParsed(PlayerUtil.getVersion(player)))
            .audiencePlaceholder("player_edition", (player, _, _) ->
                Tag.preProcessParsed(PlayerUtil.getEdition(player).name()))
            .build();
    }
}
