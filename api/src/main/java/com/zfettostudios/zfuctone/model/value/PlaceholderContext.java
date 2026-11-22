package com.zfettostudios.zfuctone.model.value;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * Контекст для плейсхолдеров.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PlaceholderContext {

    static @NonNull PlaceholderContext build() {
        return FactoryProvider.modelFactory().buildPlaceholderContext();
    }
    static @NonNull PlaceholderContext build(@NonNull String rawtext) {
        return FactoryProvider.modelFactory().buildPlaceholderContext(rawtext);
    }

    /**
     * Сохраняет значение по ключу в контексте.
     *
     * @param key ключ параметра.
     * @param value сохраняемое значение.
     * @param <T> тип значения.
     * @return сохранённое значение.
     */
    <T> T set(@NonNull String key, @NonNull T value);

    /**
     * Возвращает значение по ключу, приведённое к указанному типу.
     *
     * @param key ключ параметра.
     * @param target целевой класс значения.
     * @param <T> целевой тип.
     * @return значение указанного типа или {@code null}, если оно не найдено или не совпадает по типу.
     */
    @Nullable <T> T get(@NonNull String key, @NonNull Class<T> target);

    /**
     * Возвращает значение по ключу без приведения типа.
     *
     * @param key ключ параметра.
     * @return найденное значение или {@code null}.
     */
    @Nullable Object get(@NonNull String key);

    /**
     * Удаляет и возвращает значение по ключу, приведённое к указанному типу.
     *
     * @param key ключ параметра.
     * @param target целевой класс значения.
     * @param <T> целевой тип.
     * @return удалённое значение указанного типа или {@code null}, если оно отсутствовало или не совпадает по типу.
     */
    @Nullable <T> T remove(@NonNull String key, @NonNull Class<T> target);

    /**
     * Удаляет и возвращает значение по ключу без приведения типа.
     *
     * @param key ключ параметра.
     * @return удалённое значение или {@code null}.
     */
    @Nullable Object remove(@NonNull String key);

    /**
     * Устанавливает отправителя.
     *
     * @param sender отправитель.
     * @return текущий экземпляр контекста.
     */
    PlatformPlayer setSender(@NonNull PlatformPlayer sender);

    /**
     * Получает отправителя.
     *
     * @return отправитель или {@code null}, если не задан.
     */
    @Nullable PlatformPlayer getSender();

    /**
     * Устанавливает сырой текст.
     *
     * @param rawText сырой текст.
     * @return текущий экземпляр контекста.
     */
    String setRawText(@NonNull String rawText);

    /**
     * Получает сырой текст.
     *
     * @return сырой текст или {@code null}, если не задан.
     */
    @Nullable String getRawText();

    /**
     * Получает сырую карты контекста.
     *
     * @return сырая карта контекста, без отправителя и сырого текста.
     */
    @NonNull Map<String, Object> getRawContext();
}