package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;

public abstract class ModuleCommand {
    protected static final CommandRegistry commandRegistry = ZFuctoneProvider.get().commandRegistry();
    protected static final MessageDispatcher messageDispatcher = ZFuctoneProvider.get().messageDispatcher();
}
