package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.adapter.PlatformEntityAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import com.zfettostudios.zfuctone.service.PlatformPlayerService;
import org.jspecify.annotations.NonNull;

public interface ZFuctone extends Module<ZFuctone> {
    @NonNull <L> L getLoader(Class<L> loader);
    @NonNull Object getLoader();

    ZFuctone reload() throws ReloadException;

    PlatformType getPlatformType();

    ZLogger logger();

    FileFacade fileFacade();

    TaskScheduler taskScheduler();

    PlatformPlayerService playerService();

    PlatformPlayerAdapter playerAdapter();
    PlatformServerAdapter serverAdapter();
    PlatformEntityAdapter entityAdapter();

    Integration integration();

    MessageDispatcher messageDispatcher();

    CommandRegistry commandRegistry();
}
