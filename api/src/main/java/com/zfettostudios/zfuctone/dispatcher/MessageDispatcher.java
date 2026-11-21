package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

/**
 * Отправляет локализованные сообщения заданным получателям.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface MessageDispatcher {

    /**
     * Отправляет сообщение указанным получателям с учётом их локализации.
     *
     * @param sender отправитель для контекста сообщения.
     * @param getMessage функция получения сообщения из локализации получателя.
     * @param recipients получатели.
     * @return текущий экземпляр диспетчера.
     */
    MessageDispatcher dispatch(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage,
                               @NonNull PlatformPlayer... recipients);

    /**
     * Отправляет сообщение всем игрокам, в том числе и консоли, с учётом их локализации.
     *
     * @param sender отправитель для контекста сообщения.
     * @param getMessage функция получения сообщения из локализации получателя.
     * @return текущий экземпляр диспетчера.
     */
    MessageDispatcher dispatchEveryone(@NonNull PlatformPlayer sender, @NonNull Function<Localization, String> getMessage);
}