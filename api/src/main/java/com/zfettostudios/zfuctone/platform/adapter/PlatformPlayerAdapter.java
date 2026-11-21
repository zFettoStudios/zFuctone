package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * Адаптер для работы с нативным объектом игрока платформы.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface PlatformPlayerAdapter {

    /**
     * Возвращает нативный объект игрока по UUID, приведённый к указанному типу.
     *
     * @param uuid уникальный идентификатор игрока.
     * @param target целевой класс объекта игрока.
     * @param <T> целевой тип.
     * @return объект игрока указанного типа или {@code null}, если он не найден или не совпадает по типу.
     */
    @Nullable <T> T getPlatformPlayer(@NonNull UUID uuid, @NonNull Class<T> target);

    /**
     * Возвращает нативный объект игрока по UUID без приведения типа.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return нативный объект игрока или {@code null}, если не найден.
     */
    @Nullable Object getPlatformPlayer(@NonNull UUID uuid);

    /**
     * Проверяет, является ли объект нативным игроком платформы.
     *
     * @param platformPlayer проверяемый объект.
     * @return {@code true}, если объект является игроком платформы.
     */
    boolean isPlatformPlayer(Object platformPlayer);

    /**
     * Возвращает UUID игрока по его нику.
     *
     * @param nickname ник игрока.
     * @return UUID игрока или {@code null}, если не найден.
     */
    @Nullable UUID getUUIDFromNickname(@NonNull String nickname);

    /**
     * Выполняет команду от имени игрока платформы.
     *
     * @param platformPlayer нативный объект игрока.
     * @param command выполняемая команда.
     * @return текущий экземпляр адаптера.
     */
    @NonNull PlatformPlayerAdapter executeCommand(@NonNull Object platformPlayer, @NonNull String command);

    /**
     * Возвращает ник игрока.
     *
     * @param platformPlayer нативный объект игрока.
     * @return ник игрока.
     */
    @NonNull String getNickname(@NonNull Object platformPlayer);

    /**
     * Возвращает UUID игрока.
     *
     * @param platformPlayer нативный объект игрока.
     * @return UUID игрока или {@code null}, если определить не удалось.
     */
    @Nullable UUID getUUID(@NonNull Object platformPlayer);

    /**
     * Возвращает задержку соединения игрока в миллисекундах.
     *
     * @param platformPlayer нативный объект игрока.
     * @return пинг игрока.
     */
    int getPing(@NonNull Object platformPlayer);

    /**
     * Возвращает код локали игрока.
     *
     * @param platformPlayer нативный объект игрока.
     * @return код локали.
     */
    @NonNull String getLocale(@NonNull Object platformPlayer);

    /**
     * Проверяет, имеет ли игрок права оператора.
     *
     * @param platformPlayer нативный объект игрока.
     * @return {@code true}, если игрок является оператором.
     */
    boolean isOperator(@NonNull Object platformPlayer);

    /**
     * Проверяет, крадётся ли игрок.
     *
     * @param platformPlayer нативный объект игрока.
     * @return {@code true}, если игрок в приседе.
     */
    boolean isSneaking(@NonNull Object platformPlayer);

    /**
     * Проверяет, мёртв ли игрок.
     *
     * @param platformPlayer нативный объект игрока.
     * @return {@code true}, если игрок мёртв.
     */
    boolean isDead(@NonNull Object platformPlayer);

    /**
     * Проверяет, находится ли игрок в сети.
     *
     * @param platformPlayer нативный объект игрока.
     * @return {@code true}, если игрок онлайн.
     */
    boolean isOnline(@NonNull Object platformPlayer);

    /**
     * Проверяет наличие права у игрока.
     *
     * @param platformPlayer нативный объект игрока.
     * @param permission проверяемое право.
     * @return {@code true}, если право выдано.
     */
    boolean hasPermission(@NonNull Object platformPlayer, @NonNull String permission);

    /**
     * Возвращает игровой режим игрока.
     *
     * @param platformPlayer нативный объект игрока.
     * @return игровой режим игрока.
     */
    @NonNull PlayerGameMode getGamemode(@NonNull Object platformPlayer);

    /**
     * Возвращает название мира, в котором находится игрок.
     *
     * @param platformPlayer нативный объект игрока.
     * @return название мира.
     */
    @NonNull String getWorldName(@NonNull Object platformPlayer);

    /**
     * Возвращает тип окружения мира, в котором находится игрок.
     *
     * @param platformPlayer нативный объект игрока.
     * @return тип окружения мира.
     */
    @NonNull String getWorldEnvironment(@NonNull Object platformPlayer);
}