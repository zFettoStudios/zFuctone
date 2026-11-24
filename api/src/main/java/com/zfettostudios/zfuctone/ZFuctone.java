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

public interface ZFuctone extends Toggleable<ZFuctone> {
    @NonNull <L> L getLoader(Class<L> loader);

    @NonNull Object getLoader();

    @NonNull ZFuctone reload() throws ReloadException;

    @NonNull PlatformType getPlatformType();

    @NonNull ZLogger logger();

    @NonNull FileFacade fileFacade();

    @NonNull TaskScheduler taskScheduler();

    @NonNull PlatformPlayerService playerService();

    @NonNull PlatformPlayerAdapter playerAdapter();

    @NonNull PlatformServerAdapter serverAdapter();

    @NonNull MessageDispatcher messageDispatcher();

    @NonNull ReflectionResolver reflectionResolver();

    @NonNull ModuleController moduleController();

    @NonNull LibraryResolver libraryResolver();

    @NonNull PermissionRegistry permissionRegistry();

    @NonNull PlaceholderRegistry placeholderRegistry();

    @NonNull ListenerRegistry listenerRegistry();

    @NonNull CommandRegistry commandRegistry();
}