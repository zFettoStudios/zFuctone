package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.util.LazyInstance;

public abstract class ModuleCommand {
    protected static final LazyInstance<CommandRegistry> commandRegistry = LazyInstance.build(ZFuctone::commandRegistry);
    protected static final LazyInstance<MessageDispatcher> messageDispatcher = LazyInstance.build(ZFuctone::messageDispatcher);
}
