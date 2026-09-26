package com.zfettostudios.zfuctone.bukkit.sender;

import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;

public class ZConsole {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    private final ConsoleCommandSender console;
    @Setter
    @Getter
    private Localization localization;

    public ZConsole(ConsoleCommandSender console) {
        this.console = console;
    }

    public void sendMessage(String... message) {
        console.sendMessage(mm.deserialize(StringUtil.join(message)));
    }

    public void sendMessage(Component message) {
        console.sendMessage(message);
    }
}
