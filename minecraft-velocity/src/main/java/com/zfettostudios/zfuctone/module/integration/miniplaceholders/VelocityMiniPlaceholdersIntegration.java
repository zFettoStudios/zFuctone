package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class VelocityMiniPlaceholdersIntegration implements IntegrationHook<VelocityMiniPlaceholdersIntegration> {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    private final TaskScheduler taskScheduler = ZFuctoneProvider.get().taskScheduler();

    private final IntegrationConfig.MiniPlaceholders config = ZFuctoneProvider.get().fileFacade().integration().miniplaceholders();
    private final Expansion expansion = createExpansion();

    @Override
    public @NonNull VelocityMiniPlaceholdersIntegration hook() {
        taskScheduler.runSync(expansion::register).join();
        return this;
    }

    @Override
    public @NonNull VelocityMiniPlaceholdersIntegration unhook() {
        taskScheduler.runSync(expansion::unregister).join();
        return this;
    }

    private Expansion createExpansion() {
        Expansion.Builder builder = Expansion.builder(config.identifier())
            .author(BuildConstant.PROJECT_NAME)
            .version(BuildConstant.PROJECT_VERSION);

        for (Map<String, Function<PlaceholderContext, String>> modulePlaceholders
            : placeholderRegistry.getPlaceholders().values()) {
            for (Map.Entry<String, Function<PlaceholderContext, String>> placeholder : modulePlaceholders.entrySet()) {
                builder.audiencePlaceholder(placeholder.getKey(), (Audience audience, ArgumentQueue argument, Context context) -> {
                    Optional<UUID> uuid = audience.get(Identity.UUID);
                    if (uuid.isEmpty()) return Tag.preProcessParsed("");

                    PlatformPlayer sender = playerService.getPlatformPlayer(uuid.get());
                    if (sender == null) return Tag.preProcessParsed("");

                    PlaceholderContext placeholderContext = PlaceholderContext.build();
                    placeholderContext.setSender(sender);

                    return Tag.preProcessParsed(placeholder.getValue().apply(placeholderContext));
                });
            }
        }

        return builder.build();
    }
}
