package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.exception.NotInitializedException;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Провайдер фабрик.
 *
 * @since 0.1
 * @author RandomShel
 */

public abstract class BuilderProvider {

    /**
     * Фабрика создания игрока платформы по UUID.
     */
    protected static volatile Function<@NonNull UUID, @Nullable PlatformPlayer> buildPlatformPlayerFromUUID;

    /**
     * Фабрика создания игрока платформы по нативному объекту.
     */
    protected static volatile Function<@NonNull Object, @Nullable PlatformPlayer> buildPlatformPlayerFromRawPlayer;

    /**
     * Фабрика создания пустого контекста плейсхолдеров.
     */
    protected static volatile Supplier<@NonNull PlaceholderContext> buildPlaceholderContext;

    /**
     * Фабрика создания контекста плейсхолдеров из исходного текста.
     */
    protected static volatile Function<@NonNull String, @NonNull PlaceholderContext> buildPlaceholderContextFromRawText;

    /**
     * Получение пустого {@link IntegrationHook}.
     */
    protected static volatile Supplier<IntegrationHook<?>> getEmptyIntegrationHook;

    /**
     * Получает экземпляр IntegrationHook. Вместо этого метода используйте {@link IntegrationHook#empty()}.
     *
     * @return экземпляр {@link IntegrationHook}.
     */
    public static @NonNull IntegrationHook<?> getEmptyIntegrationHook() {
        if (getEmptyIntegrationHook == null)
            throw new NotInitializedException("IntegrationHook builder has not been initialized");

        return getEmptyIntegrationHook.get();
    }

    /**
     * Создаёт экземпляр игрока платформы по UUID. Вместо этого метода используйте {@link PlatformPlayer#build(UUID)}.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если игрок не найден.
     */
    public static @Nullable PlatformPlayer buildPlatformPlayer(@NonNull UUID uuid) {
        if (buildPlatformPlayerFromUUID == null)
            throw new NotInitializedException("PlatformPlayer builder has not been initialized");

        return buildPlatformPlayerFromUUID.apply(uuid);
    }

    /**
     * Создаёт экземпляр игрока платформы по нативному объекту. Вместо этого метода используйте {@link PlatformPlayer#build(Object)}.
     *
     * @param rawPlayer нативный объект игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если объект не поддерживается.
     */
    public static @Nullable PlatformPlayer buildPlatformPlayer(@NonNull Object rawPlayer) {
        if (buildPlatformPlayerFromRawPlayer == null)
            throw new NotInitializedException("PlatformPlayer builder has not been initialized");

        return buildPlatformPlayerFromRawPlayer.apply(rawPlayer);
    }

    /**
     * Создаёт пустой контекст плейсхолдеров. Вместо этого метода используйте {@link PlaceholderContext#build()}.
     *
     * @return новый контекст плейсхолдеров.
     */
    public static @NonNull PlaceholderContext buildPlaceholderContext() {
        if (buildPlaceholderContext == null)
            throw new NotInitializedException("PlaceholderContext builder has not been initialized");

        return buildPlaceholderContext.get();
    }

    /**
     * Создаёт контекст плейсхолдеров на основе исходного текста. Вместо этого метода используйте {@link PlaceholderContext#build(String)}.
     *
     * @param rawText исходный текст с плейсхолдерами.
     * @return новый контекст плейсхолдеров.
     */
    public static @NonNull PlaceholderContext buildPlaceholderContextFromRawText(@NonNull String rawText) {
        if (buildPlaceholderContextFromRawText == null)
            throw new NotInitializedException("PlaceholderContext builder has not been initialized");

        return buildPlaceholderContextFromRawText.apply(rawText);
    }
}