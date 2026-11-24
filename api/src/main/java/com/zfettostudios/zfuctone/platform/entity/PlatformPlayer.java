package com.zfettostudios.zfuctone.platform.entity;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * Обертка над игроком платформы.
 *
 * @since 1.0
 * @author RandomShel
 */
public interface PlatformPlayer extends Audience {

    /**
     * Создаёт экземпляр игрока по UUID.
     *
     * @param uuid уникальный идентификатор игрока.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если игрок не найден.
     */
    static @Nullable PlatformPlayer build(@NonNull UUID uuid) {
        return FactoryProvider.playerFactory().build(uuid);
    }

    /**
     * Создаёт экземпляр игрока по объекту платформенного игрока.
     *
     * @param rawPlayer объект игрока платформы.
     * @return экземпляр {@link PlatformPlayer} или {@code null}, если объект не является игроком платформы.
     */
    static @Nullable PlatformPlayer build(@NonNull Object rawPlayer) {
        return FactoryProvider.playerFactory().build(rawPlayer);
    }

    /**
     * Получает объект игрока платформы, приведённый к указанному типу.
     *
     * @param type целевой класс платформы объекта.
     * @param <T> целевой тип.
     * @return объект игрока указанного типа.
     */
    @NonNull <T> T getRawPlayer(@NonNull Class<T> type);

    /**
     * Получает объект игрока платформы.
     *
     * @return объект игрока.
     */
    @NonNull Object getRawPlayer();

    /**
     * Возвращает UUID игрока.
     *
     * @return уникальный идентификатор игрока.
     */
    @NonNull UUID uuid();

    /**
     * Получает ник игрока.
     *
     * @return ник игрока.
     */
    @NonNull String getNickname();

    /**
     * Возвращает отображаемый ник игрока.
     *
     * @return отображаемый ник игрока.
     */
    @NonNull String getDisplayNickname();

    /**
     * Получает код языка.
     *
     * @return код языка.
     */
    @NonNull String getLocale();

    /**
     * Получает IP-адрес игрока.
     *
     * @return IP-адрес игрока.
     */
    @NonNull String getIP();

    /**
     * Получает протокол версии игрока, с учетом интеграции с ViaVersion, Floodgate и Geyser.
     *
     * @return протокол версии игрока.
     */
    int getProtocolVersion();

    /**
     * Получает версию игрока, с учетом интеграции с ViaVersion, Floodgate и Geyser.
     *
     * @return версию игрока.
     */
    @NonNull String getVersion();

    /**
     * Получает версию игрока из локализации, с учетом интеграции с ViaVersion, Floodgate и Geyser.
     *
     * @return версия игрока из локализации.
     */
    @NonNull String getDisplayVersion();

    /**
     * Получает издание игрока, с учетом интеграции с Floodgate и Geyser.
     *
     * @return издание игрока.
     */
    @NonNull PlayerEdition getEdition();

    /**
     * Получает издание игрока из локализации, с учетом интеграции с Floodgate и Geyser.
     *
     * @return издание игрока из локализации.
     */
    @NonNull String getDisplayEdition();

    /**
     * Проверяет, имеет ли игрок права оператора.
     *
     * @return {@code true}, если игрок является оператором.
     */
    boolean isOperator();

    /**
     * Проверяет, крадётся ли игрок.
     *
     * @return {@code true}, если игрок в приседе.
     */
    boolean isSneaking();

    /**
     * Проверяет, мёртв ли игрок.
     *
     * @return {@code true}, если игрок мёртв.
     */
    boolean isDead();

    /**
     * Проверяет, находится ли игрок в сети.
     *
     * @return {@code true}, если игрок онлайн.
     */
    boolean isOnline();

    /**
     * Возвращает задержку соединения игрока.
     *
     * @return пинг игрока.
     */
    int getPing();

    /**
     * Проверяет наличие права у игрока, с учетом интеграции с LuckPerms.
     *
     * @param permission проверяемое право.
     * @return {@code true}, если право выдано.
     */
    boolean hasPermission(@NonNull String permission);

    /**
     * Возвращает игровой режим игрока.
     *
     * @return игровой режим игрока.
     */
    @NonNull PlayerGameMode getGamemode();

    /**
     * Возвращает название мира, в котором находится игрок.
     *
     * @return название мира.
     */
    @NonNull String getWorldName();

    /**
     * Возвращает тип окружения мира, в котором находится игрок.
     *
     * @return тип окружения мира.
     */
    @NonNull String getWorldEnvironment();

    /**
     * Устанавливает отображаемый ник игрока.
     *
     * @param displayNickname новый отображаемый ник.
     * @return текущий экземпляр игрока.
     */
    @NonNull PlatformPlayer setDisplayNickname(@NonNull String displayNickname);

    /**
     * Отправляет игроку сообщение.
     *
     * @param message отправляемое сообщение.
     * @return текущий экземпляр игрока.
     */
    @NonNull PlatformPlayer dispatchMessage(@NonNull Component message);

    /**
     * Выполняет команду от имени игрока.
     *
     * @param command выполняемая команда.
     * @return текущий экземпляр игрока.
     */
    @NonNull PlatformPlayer executeCommand(@NonNull String command);

    /**
     * Отключает игрока от сервера с указанной причиной.
     *
     * @param reason причина отключения.
     * @return текущий экземпляр игрока.
     */
    @NonNull PlatformPlayer kick(@NonNull Component reason);

    boolean isPlayer();
    boolean isConsole();
}