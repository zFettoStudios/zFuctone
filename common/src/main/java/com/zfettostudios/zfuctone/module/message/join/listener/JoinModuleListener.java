package com.zfettostudios.zfuctone.module.message.join.listener;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.annotation.event.EventHandler;
import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.model.event.player.PlayerJoinEvent;
import com.zfettostudios.zfuctone.module.message.join.JoinModule;
import com.zfettostudios.zfuctone.util.LazyInstance;

@RequiredArgsConstructor
public class JoinModuleListener {
    private final LazyInstance<MessageDispatcher> messageDispatcher = LazyInstance.build(ZFuctone::messageDispatcher);
    private final JoinModule joinModule;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (joinModule.isDisable() || !event.player().hasPermission(joinModule.permission().name())) return;

        event.removeJoinMessage();

        messageDispatcher.get().dispatchEveryone(event.player(), GetMessage.ofLocalization(localization -> localization.message().join().format().asString()));
    }
}
