package com.zfettostudios.zfuctone.dispatcher;

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
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MessageDispatcherImpl implements MessageDispatcher {
    private Integration integration() {
        return ZFuctoneAPI.get().integration();
    }

    private TaskScheduler taskScheduler() {
        return ZFuctoneAPI.get().taskScheduler();
    }

    private ZLogger logger() {
        return ZFuctoneAPI.get().logger();
    }

    private FileFacade fileFacade() {
        return ZFuctoneAPI.get().fileFacade();
    }

    private PlatformPlayerAdapter playerAdapter() {
        return ZFuctoneAPI.get().playerAdapter();
    }

    private PlatformServerAdapter serverAdapter() {
        return ZFuctoneAPI.get().serverAdapter();
    }

    @Override
    public MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage, @NonNull PlatformPlayer... recipients) {
        taskScheduler().runAsync(() -> {
            CompletableFuture<?>[] futures = new CompletableFuture<?>[recipients.length];

            for (int i = 0; i < recipients.length; i++) {
                Object recipient = recipients[i];

                futures[i] = taskScheduler().runSync(() ->
                    playerAdapter().dispatchMessage(recipient,
                        integration().miniPlaceholders().setPlaceholders(
                            sender,
                            integration().placeholderAPI().setPlaceholders(sender, getMessage.apply(
                                fileFacade().localizationOrDefault(playerAdapter().getLocale(recipient), LocalizationType.PLAYER))))));
            }

            taskScheduler().runAsyncAllOf(futures);
        });

        return this;
    }

    @Override
    public MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage) {
        List<Object> recipients = serverAdapter().getOnlinePlatformPlayers();

        taskScheduler().runAsync(() -> {
            CompletableFuture<?>[] futures = new CompletableFuture<?>[recipients.size() + 1];

            futures[0] = taskScheduler().runSync(() -> {
                logger().info(integration().miniPlaceholders().setPlaceholders(
                    sender,
                    integration().placeholderAPI().setPlaceholders(sender,
                        getMessage.apply(fileFacade().localization(LocalizationType.CONSOLE)))));
            });

            for (int i = 1; i < recipients.size(); i++) {
                Object recipient = recipients.get(i);

                futures[i] = taskScheduler().runSync(() ->
                    playerAdapter().dispatchMessage(recipient,
                        integration().miniPlaceholders().setPlaceholders(
                            sender,
                            integration().placeholderAPI().setPlaceholders(sender, getMessage.apply(
                                fileFacade().localizationOrDefault(playerAdapter().getLocale(recipient), LocalizationType.PLAYER))))));
            }

            taskScheduler().runAsyncAllOf(futures);
        });

        return this;
    }
}
