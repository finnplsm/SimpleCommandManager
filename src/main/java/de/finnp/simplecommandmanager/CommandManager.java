package de.finnp.simplecommandmanager;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager {

    private final CommandSender sender;
    private final String[] arguments;
    private final String label;
    private CommandApi commandApi;

    public CommandManager(@NotNull final CommandSender sender, @NotNull final String[] arguments, @NotNull final String label) {
        this.sender = sender;
        this.arguments = arguments;
        this.label = label;
        setCommandApi(new CommandApi());
    }

    @NotNull
    public CommandSender getSender() {
        return sender;
    }

    @NotNull
    public String[] getArguments() {
        return arguments;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @NotNull
    private CommandApi getCommandApi() {
        return commandApi;
    }

    private void setCommandApi(@NotNull final CommandApi commandApi) {
        this.commandApi=commandApi;
    }
}
