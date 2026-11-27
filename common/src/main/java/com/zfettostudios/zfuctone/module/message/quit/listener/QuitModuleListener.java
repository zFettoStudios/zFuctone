package com.zfettostudios.zfuctone.module.message.quit.listener;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.module.message.quit.QuitModule;
import com.zfettostudios.zfuctone.util.LazyInstance;

@RequiredArgsConstructor
public class QuitModuleListener {
    private final LazyInstance<MessageDispatcher> messageDispatcher = LazyInstance.build(ZFuctone::messageDispatcher);
    private final QuitModule quitModule;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (quitModule.isDisable() || !event.player().hasPermission(quitModule.permission().name())) return;

        event.removeQuitMessage();

        messageDispatcher.get().dispatchEveryone(event.player(), GetMessage.ofLocalization(localization -> localization.message().quit().format().asString()));
    }
}
