package de.finnp.simplecommandmanager;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


    public void setTabCompletion(@NotNull final String argument, @NotNull final List<String> completions) {
        getCommandApi().addTabCompletion(argument, completions);
    }

    public void setTabCompletion(@NotNull final String argument,@NotNull final String... completions) {
        getCommandApi().addTabCompletion(argument, Arrays.asList(completions));
    }

    @NotNull
    public List<@NotNull String> getTabCompletion(@NotNull final String argument) {
        return commandApi.getTabCompletions().get(argument);
    }

    @NotNull
    public Map<@NotNull String, @NotNull List<@NotNull String>> getTabCompletions(@NotNull final String argument) {
        return  commandApi.getTabCompletions();
    }
}
