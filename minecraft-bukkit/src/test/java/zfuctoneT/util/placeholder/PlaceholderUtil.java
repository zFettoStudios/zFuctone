package zfuctoneT.util.placeholder;

import zfuctoneT.ZFuctone;
import zfuctoneT.integration.Integration;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class PlaceholderUtil {
    public static Component setPlaceholders(PlaceholderContext context) {
        Integration integration = ZFuctone.get(Integration.class);

        return context.getRawText().map(string ->
                integration.placeholderAPI().setPlaceholders(context.getSender().orElse(null), string))
            .map(string -> MiniMessage.miniMessage().deserialize(
                string,
                integration.miniPlaceholders().getAudienceGlobalPlaceholders(),
                integration.miniPlaceholders().getAudiencePlaceholders(),
                integration.miniPlaceholders().getGlobalPlaceholders(),
                context
            ))
            .orElseGet(Component::empty);
    }
}
