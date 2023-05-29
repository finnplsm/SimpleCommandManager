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
    private final Map<String, List<String>> tabCompletions;

    public CommandManager(@NotNull final CommandSender sender, @NotNull final String[] arguments, @NotNull final String label, @NotNull final Map<String, List<String>> tabCompletions) {
        this.sender = sender;
        this.arguments = arguments;
        this.label = label;
        this.tabCompletions = tabCompletions;
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

    public void setTabCompletion(@NotNull final String argument,@NotNull final List<String> completions) {
        tabCompletions.put(argument, completions);
    }

    public void setTabCompletion(@NotNull final String argument,@NotNull final String... completions) {
        tabCompletions.put(argument, Arrays.asList(completions));
    }

    @NotNull
    public List<String> getTabCompletion(@NotNull final String argument) {
        return tabCompletions.get(argument);
    }
}
