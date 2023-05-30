package de.finnp.simplecommandmanager;

import de.finnp.simplecommandmanager.annotation.CommandHandler;
import de.finnp.simplecommandmanager.annotation.CommandProperties;
import de.finnp.simplecommandmanager.annotation.List;
import de.finnp.simplecommandmanager.annotation.TabCompletion;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements Command{
    @CommandHandler("test")
    @TabCompletion({@List({"String1", "String2", "String3"}), @List({"String1", "String2", "String3"})})
    @CommandProperties(type = CommandType.UNIVERSAL, usage = "/test", aliases = {"testcommand", "t"})
    private void onCommand(@NotNull final CommandManager commandManager){
        final String label=commandManager.getLabel();
        final String[] args=commandManager.getArguments();
        final CommandSender commandSender=commandManager.getSender();
    }
}
