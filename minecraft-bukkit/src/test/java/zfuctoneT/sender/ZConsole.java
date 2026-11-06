package zfuctoneT.sender;

import zfuctoneT.ZFuctone;
import zfuctoneT.file.FileFacade;
import zfuctoneT.model.Module;
import zfuctoneT.model.config.Config;
import zfuctoneT.model.config.Localization;
import zfuctoneT.util.placeholder.PlaceholderContext;
import zfuctoneT.util.placeholder.PlaceholderUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ZConsole implements Module<ZConsole> {
    @Delegate
    @Getter(AccessLevel.NONE)
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    @NonFinal Localization localization;

    @Override
    public ZConsole enable() {
        return reloadLocalization();
    }

    @Override
    public ZConsole disable() {
        return this;
    }

    @Override
    public ZConsole reload() {
        return this;
    }

    private ZConsole reloadLocalization() {
        localization = ZFuctone.get(FileFacade.class).localizations().get(ZFuctone.get(Config.class).language().console().type());
        return this;
    }

    public ZConsole sendMessage(String... messages) {
        if (messages.length == 0) console.sendMessage("");
        else if (messages.length == 1) console.sendMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext().setRawText(messages[0])));
        else console.sendMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext().setRawText(String.join("", messages))));

        return this;
    }
}
