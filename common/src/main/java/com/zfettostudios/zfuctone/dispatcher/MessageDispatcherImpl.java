package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.MiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.PlaceholderAPIModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Function;

public class MessageDispatcherImpl implements MessageDispatcher {
    private final TaskScheduler taskScheduler = ZFuctoneAPI.get().taskScheduler();
    private final ZLogger logger = ZFuctoneAPI.get().logger();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final PlatformPlayerService playerService = ZFuctoneAPI.get().playerService();

    private MiniPlaceholdersModule miniPlaceholders() {
        return ZFuctoneAPI.get().moduleController().getIntegration(MiniPlaceholdersModule.class);
    }

    private PlaceholderAPIModule placeholderAPI() {
        return ZFuctoneAPI.get().moduleController().getIntegration(PlaceholderAPIModule.class);
    }

    @Override
    public MessageDispatcher dispatch(@NonNull PlatformPlayer sender,
                                      @NonNull Function<Localization, String> getMessage,
                                      @NonNull PlatformPlayer... recipients) {
        for (PlatformPlayer recipient : recipients) dispatchAsyncToPlayer(sender, recipient, getMessage);

        return this;
    }

    @Override
    public MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender,
                                              @NonNull Function<Localization, String> getMessage) {
        List<PlatformPlayer> recipients = playerService.getOnlinePlayers();

        taskScheduler.runAsync(() -> logger.info(
            formatPlaceholders(sender, getMessage.apply(fileFacade.localization(LocalizationType.CONSOLE)))));

        for (PlatformPlayer recipient : recipients) dispatchAsyncToPlayer(sender, recipient, getMessage);

        return this;
    }

    private void dispatchAsyncToPlayer(@NonNull PlatformPlayer sender,
                                       @NonNull PlatformPlayer recipient,
                                       @NonNull Function<Localization, String> getMessage) {
        taskScheduler.runAsync(() -> {
            String message = getMessage.apply(fileFacade.localizationOrDefault(recipient.getLocale(),
                LocalizationType.PLAYER));

            taskScheduler.runSync(() ->
                recipient.dispatchMessage(formatPlaceholders(sender, message)));
        });
    }

    private Component formatPlaceholders(@NonNull PlatformPlayer sender, @NonNull String message) {
        return miniPlaceholders().setPlaceholders(sender, placeholderAPI().setPlaceholders(sender, message));
    }
}