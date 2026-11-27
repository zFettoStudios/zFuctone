package com.zfettostudios.zfuctone.module.command.zfuctone.command;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.constant.command.CommandArgumentName;
import com.zfettostudios.zfuctone.module.command.ModuleCommand;
import com.zfettostudios.zfuctone.module.command.zfuctone.ZFuctoneModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.Optional;

@UtilityClass
public class ZFuctoneModuleCommand extends ModuleCommand {
    private final LazyInstance<ZFuctoneModule> module = LazyInstance.build(zfuctone -> zfuctone.moduleController().get(ZFuctoneModule.class));

    public void register() {
        commandRegistry.get().register(module.get().moduleClass(), manager ->
            manager.commandBuilder(module.get().config().aliases().getFirst(), module.get().config().aliases().toArray(new String[0]))
                .permission(module.get().permission().name())
                .optional(CommandArgumentName.FIRST, StringParser.stringParser(), SuggestionProvider.suggestingStrings(module.get().config().reload().aliases().toArray(new String[0])))
                .handler(ZFuctoneModuleCommand::handler)
        );
    }

    private void handler(CommandContext<PlatformPlayer> context) {
        PlatformPlayer sender = context.sender();
        Optional<String> argumentOptional = context.optional(CommandArgumentName.FIRST);
        if (argumentOptional.isEmpty()) return;

        String argument = argumentOptional.get();

        if (module.get().config().reload().aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(argument))) {
            try {
                ZFuctoneProvider.get().reload();

                messageDispatcher.get().dispatch(sender, GetMessage.ofLocalization(localization ->
                    localization.command().zfuctone().reload().asString()), sender);
            } catch (ReloadException e) {
                ZFuctoneProvider.get().logger().warn("Reload failed: {}", e.getMessage());

                messageDispatcher.get().dispatch(sender, GetMessage.ofLocalization(localization ->
                    localization.command().zfuctone().failedReload().asString()), sender);
            }
        }
    }
}