package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import com.zfettostudios.zfuctone.util.LazyInstance;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MessageDispatcherImpl implements MessageDispatcher {
    private final TaskScheduler taskScheduler = ZFuctoneProvider.get().taskScheduler();
    private final ZLogger logger = ZFuctoneProvider.get().logger();
    private final PlatformPlayerService playerService = ZFuctoneProvider.get().playerService();
    private final LazyInstance<MiniPlaceholdersModule> miniPlaceholders = LazyInstance.build(zfuctone -> zfuctone.moduleController().get(MiniPlaceholdersModule.class));
    private final LazyInstance<PlaceholderAPIModule> placeholderAPI = LazyInstance.build(zfuctone -> zfuctone.moduleController().get(PlaceholderAPIModule.class));

    @Override
    public @NonNull MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull GetMessage getMessage,
                                               @NonNull PlatformPlayer... recipients) {
        for (PlatformPlayer recipient : recipients) dispatchAsyncToPlayer(sender, recipient, getMessage);

        return this;
    }

    @Override
    public @NonNull MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull GetMessage getMessage) {
        List<PlatformPlayer> recipients = playerService.getOnlinePlayers();

        taskScheduler.runAsync(() -> {
            Component component = formatPlaceholders(sender, getMessage.apply(logger));
            if (component == null) {
                logger.warn("The message component is null for the Console (check the localization configuration)");
                return;
            }

            logger.info(component);
        });

        for (PlatformPlayer recipient : recipients) dispatchAsyncToPlayer(sender, recipient, getMessage);

        return this;
    }

    private void dispatchAsyncToPlayer(PlatformPlayer sender, PlatformPlayer recipient, GetMessage getMessage) {
        taskScheduler.runAsync(() -> {
            String message = getMessage.apply(sender);

            taskScheduler.runSync(() -> {
                Component component = formatPlaceholders(sender, message);
                if (component == null) {
                    logger.warn("The message component is null for the recipient \"{}\" (check the localization configuration)",
                        recipient.getNickname());
                    return;
                }

                recipient.dispatchMessage(component);
            });
        });
    }

    private Component formatPlaceholders(PlatformPlayer sender, String message) {
        if (message == null || message.isEmpty()) return null;

        return miniPlaceholders.get().setPlaceholders(sender, placeholderAPI.get().setPlaceholders(sender, message));
    }
}