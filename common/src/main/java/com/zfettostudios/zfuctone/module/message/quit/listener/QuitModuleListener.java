package com.zfettostudios.zfuctone.module.message.quit.listener;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.listener.ZFListener;
import com.zfettostudios.zfuctone.model.event.player.PlayerQuitEvent;
import com.zfettostudios.zfuctone.module.message.quit.QuitModule;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;

@RequiredArgsConstructor
public class QuitModuleListener implements ZFListener {
    private final MessageDispatcher messageDispatcher = ZFuctoneProvider.get().messageDispatcher();
    private final QuitModule quitModule;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (quitModule.isDisable() || !event.player().hasPermission(quitModule.permission().name())) return;

        messageDispatcher.dispatchEveryone(event.player(), GetMessage.ofLocalization(localization -> localization.message().quit().format().asString()));
    }
}
