package de.finnp.simplecommandmanager;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements Command{
    @CommandHandler("test")
    @CommandProperties(type = CommandType.UNIVERSAL, usage = "/test", aliases = {"testcommand", "t"})
    private void onCommand(@NotNull final CommandManager commandManager){
        final String label=commandManager.getLabel();
        final String[] args=commandManager.getArguments();
        final CommandSender commandSender=commandManager.getSender();
        commandManager.setTabCompletion(args[0],"Hello");
        commandManager.setTabCompletion(args[1],"World!");
    }
}
