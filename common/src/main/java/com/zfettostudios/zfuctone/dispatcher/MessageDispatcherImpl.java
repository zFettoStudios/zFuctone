package com.zfettostudios.zfuctone.dispatcher;

import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.scheduler.TaskSchedulerImpl;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MessageDispatcherImpl implements MessageDispatcher {
    TaskScheduler scheduler = ZFuctoneAPI.get(TaskSchedulerImpl.class);
    ZLogger logger = ZFuctoneAPI.get(ZLogger.class);
    FileFacade fileFacade = ZFuctoneAPI.get(FileFacade.class);
    @NonFinal PlatformPlayerAdapter playerAdapter;
    @NonFinal PlatformServerAdapter serverAdapter;
    @NonFinal Integration integration;

    @Override
    public MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage, @NonNull PlatformPlayer... recipients) {
        isNullGet();

        scheduler.runAsync(() -> {
            CompletableFuture<?>[] futures = new CompletableFuture<?>[recipients.length];

            for (int i = 0; i < recipients.length; i++) {
                Object recipient = recipients[i];

                futures[i] = scheduler.runSync(() ->
                    playerAdapter.dispatchMessage(recipient,
                        integration.miniPlaceholders().setPlaceholders(
                            sender,
                            integration.placeholderAPI().setPlaceholders(sender, getMessage.apply(
                                fileFacade.localizationOrDefault(playerAdapter.getLocale(recipient), LocalizationType.PLAYER))))));
            }

            scheduler.runAsyncAllOf(futures);
        });

        return this;
    }

    @Override
    public MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage) {
        isNullGet();

        List<Object> recipients = serverAdapter.getOnlinePlatformPlayers();

        scheduler.runAsync(() -> {
            CompletableFuture<?>[] futures = new CompletableFuture<?>[recipients.size() + 1];

            futures[0] = scheduler.runSync(() -> {
                logger.info(integration.miniPlaceholders().setPlaceholders(
                    sender,
                    integration.placeholderAPI().setPlaceholders(sender,
                        getMessage.apply(fileFacade.localization(LocalizationType.CONSOLE)))));
            });

            for (int i = 1; i < recipients.size(); i++) {
                Object recipient = recipients.get(i);

                futures[i] = scheduler.runSync(() ->
                    playerAdapter.dispatchMessage(recipient,
                        integration.miniPlaceholders().setPlaceholders(
                            sender,
                            integration.placeholderAPI().setPlaceholders(sender, getMessage.apply(
                                fileFacade.localizationOrDefault(playerAdapter.getLocale(recipient), LocalizationType.PLAYER))))));
            }

            scheduler.runAsyncAllOf(futures);
        });

        return this;
    }


    private void isNullGet() {
        if (playerAdapter == null) playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);
        if (integration == null) integration = ZFuctoneAPI.get(Integration.class);
        if (serverAdapter == null) serverAdapter = ZFuctoneAPI.get(PlatformServerAdapter.class);
    }
}
