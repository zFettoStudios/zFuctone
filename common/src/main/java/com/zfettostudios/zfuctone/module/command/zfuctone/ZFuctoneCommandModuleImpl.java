package com.zfettostudios.zfuctone.module.command.zfuctone;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.util.ComponentUtil;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;

public class ZFuctoneCommandModuleImpl implements ZFuctoneCommandModule {
    private final CommandRegistry commandRegistry = ZFuctoneAPI.get(CommandRegistry.class);
    private final PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);
    private final ZFuctone zfuctone = ZFuctoneAPI.get(ZFuctone.class);
    private final FileFacade fileFacade = ZFuctoneAPI.get(FileFacade.class);

    CommandConfig.ZFuctone commandConfig = ZFuctoneAPI.get(FileFacade.class).command().zfuctone();
    PermissionConfig.Command.ZFuctone permissionConfig = ZFuctoneAPI.get(FileFacade.class).permission().command().zfuctone();

    @Override
    public ZFuctoneCommandModule execute(ZPlayer player, CommandContext<ZPlayer> commandContext) {
        return this;
    }

    @Override
    public ZFuctoneCommandModule register() {
        if (commandConfig.aliases().isEmpty()) return this;

        commandRegistry.register(manager -> {
            Command.Builder<ZPlayer> builder = manager.commandBuilder(commandConfig.aliases().getFirst(), commandConfig.aliases().toArray(new String[0]))
                .permission(permissionConfig.name())
                .handler(this::handlerReload);

            if (!commandConfig.reload().aliases().isEmpty())
                builder.literal(commandConfig.reload().aliases().getFirst(), commandConfig.reload().aliases().toArray(new String[0]));

            return builder;
        });

        return this;
    }

    private void handlerReload(CommandContext<ZPlayer> context) {
        ZPlayer player = context.sender();
        Object platformPlayer = playerAdapter.getPlatformPlayer(player.uuid());
        Localization localization;
        if (platformPlayer != null) localization = fileFacade.localizationOrDefault(playerAdapter.getLocale(platformPlayer), LocalizationType.PLAYER);
        else localization = fileFacade.localizationOrDefault(playerAdapter.getLocale(player.uuid()), LocalizationType.PLAYER);

        try {
            zfuctone.reload();
        } catch (ReloadException e) {
            ZFuctoneAPI.get(ZLogger.class).warn("Command execution error {}", e.getMessage());
        }

        playerAdapter.dispatchMessage(player.uuid(), ComponentUtil.build(player, localization.command().zfuctone().reload()));
    }

    @Override
    public ZFuctoneCommandModule unregister() {
        commandRegistry.unregister(commandConfig.aliases().getFirst());
        return this;
    }
}
