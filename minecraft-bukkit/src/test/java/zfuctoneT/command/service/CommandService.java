package zfuctoneT.command.service;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.CommandManager;

public interface CommandService<T> {
    T register(CommandManager<CommandSourceStack> manager);
}
