package de.finnp.simplecommandmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandApi implements CommandExecutor, TabCompleter {
    private Map<@NotNull String, @NotNull List<@NotNull String>> tabCompletions;
    private List<@NotNull Object> commandHandlers;

    private de.finnp.simplecommandmanager.Command[] commands;

    public CommandApi(@NotNull final de.finnp.simplecommandmanager.Command[] commands) {
        tabCompletions = new HashMap<>();
        commandHandlers = new ArrayList<>();
        setCommands(commands);
    }
    public CommandApi() {}

    @NotNull
    private de.finnp.simplecommandmanager.Command[] getCommands() {
        return commands;
    }

    private void setCommands(@NotNull final de.finnp.simplecommandmanager.Command[] commands) {
        this.commands = commands;
    }

    public void registerCommandHandler(@NotNull final Object commandHandler) {
        commandHandlers.add(commandHandler);
    }

    public void addTabCompletion(@NotNull final String argument, @NotNull final List<@NotNull String> completions) {
        tabCompletions.put(argument, completions);
    }

    public Map<@NotNull String, @NotNull List<@NotNull String>> getTabCompletions() {
        return tabCompletions;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender, @NotNull final Command bukkitCommand, @NotNull final String label, @NotNull final String[] args) {
        for (final de.finnp.simplecommandmanager.Command command: getCommands()) {
            for (final Method method: command.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(CommandHandler.class) &&
                        method.getParameterTypes().length != 1) continue;
                if(!method.getAnnotation(CommandHandler.class).value().equalsIgnoreCase(label)) continue;
                try {
                    if (method.isAnnotationPresent(CommandProperties.class)) {
                        final CommandProperties properties = method.getAnnotation(CommandProperties.class);
                        switch (properties.type()) {
                            case UNIVERSAL -> {
                                method.invoke(command, new CommandManager(commandSender, args, label));
                            }case PLAYER -> {
                                if (commandSender instanceof Player) method.invoke(command, new CommandManager(commandSender, args, label));
                                else commandSender.sendMessage(properties.invalidSender());
                            }case CONSOLE -> {
                                if (!(commandSender instanceof Player)) method.invoke(command, new CommandManager(commandSender, args, label));
                                else commandSender.sendMessage(properties.invalidSender());
                            }default -> throw new IllegalStateException("Unexpected value: " + properties.type());
                        }
                    } else {
                        method.invoke(command, new CommandManager(commandSender, args, label));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public List<@NotNull String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command bukkitCommand, @NotNull final String label, @NotNull final String[] args) {
        if (args.length >= 1) {
            final List<@NotNull String> completions = getTabCompletions().get(args[0]);
            if (completions != null) {
                return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
            }
        }
        return null;
    }
}
