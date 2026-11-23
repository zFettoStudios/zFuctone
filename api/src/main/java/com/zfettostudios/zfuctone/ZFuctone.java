package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.model.lifecycle.Toggleable;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import com.zfettostudios.zfuctone.resolver.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import org.jspecify.annotations.NonNull;

/**
 * Ядро проекта.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface ZFuctone extends Toggleable<ZFuctone> {

    /**
     * Возвращает загрузчик платформы, приведённый к указанному типу.
     *
     * @param loader целевой класс загрузчика.
     * @param <L> целевой тип загрузчика.
     * @return экземпляр загрузчика указанного типа.
     */
    @NonNull <L> L getLoader(Class<L> loader);

    /**
     * Возвращает загрузчик платформы без приведения типа.
     *
     * @return экземпляр загрузчика платформы.
     */
    @NonNull Object getLoader();

    /**
     * Перезагружает состояние фреймворка и его компонентов.
     *
     * @return текущий экземпляр {@link ZFuctone}.
     * @throws ReloadException если при перезагрузке произошла ошибка.
     */
    ZFuctone reload() throws ReloadException;

    /**
     * Возвращает тип текущей платформы.
     *
     * @return тип платформы.
     */
    PlatformType getPlatformType();

    /**
     * Возвращает логгер фреймворка.
     *
     * @return экземпляр {@link ZLogger}.
     */
    ZLogger logger();

    /**
     * Возвращает фасад для работы с файлами и конфигурациями.
     *
     * @return экземпляр {@link FileFacade}.
     */
    FileFacade fileFacade();

    /**
     * Возвращает планировщик синхронных и асинхронных задач.
     *
     * @return экземпляр {@link TaskScheduler}.
     */
    TaskScheduler taskScheduler();

    /**
     * Возвращает сервис управления игроками платформы.
     *
     * @return экземпляр {@link PlatformPlayerService}.
     */
    PlatformPlayerService playerService();

    /**
     * Возвращает адаптер для работы с игроками платформы.
     *
     * @return экземпляр {@link PlatformPlayerAdapter}.
     */
    PlatformPlayerAdapter playerAdapter();

    /**
     * Возвращает адаптер для взаимодействия с сервером платформы.
     *
     * @return экземпляр {@link PlatformServerAdapter}.
     */
    PlatformServerAdapter serverAdapter();

    /**
     * Возвращает диспетчер отправки сообщений.
     *
     * @return экземпляр {@link MessageDispatcher}.
     */
    MessageDispatcher messageDispatcher();

    /**
     * Возвращает утилиту рефлексии для проверки классов и аннотаций.
     *
     * @return экземпляр {@link ReflectionResolver}.
     */
    ReflectionResolver reflectionResolver();

    /**
     * Возвращает контроллер управления модулями.
     *
     * @return экземпляр {@link ModuleController}.
     */
    ModuleController moduleController();

    /**
     * Возвращает загрузчик и разрешитель внешних библиотек.
     *
     * @return экземпляр {@link LibraryResolver}.
     */
    LibraryResolver libraryResolver();

    /**
     * Возвращает реестр прав доступа модулей.
     *
     * @return экземпляр {@link PermissionRegistry}.
     */
    PermissionRegistry permissionRegistry();

    /**
     * Возвращает реестр плейсхолдеров модулей.
     *
     * @return экземпляр {@link PlaceholderRegistry}.
     */
    PlaceholderRegistry placeholderRegistry();

    ListenerRegistry listenerRegistry();

    CommandRegistry commandRegistry();
}