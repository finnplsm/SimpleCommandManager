package de.finnp.simplecommandmanager;

import de.finnp.simplecommandmanager.annotation.CommandHandler;
import de.finnp.simplecommandmanager.annotation.CommandProperties;
import de.finnp.simplecommandmanager.annotation.TabCompletion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CommandApi implements CommandExecutor, TabCompleter {
    private List<@NotNull Object> commandHandlers;

    private de.finnp.simplecommandmanager.Command[] commands;

    public CommandApi(@NotNull final de.finnp.simplecommandmanager.Command[] commands) {
        commandHandlers = new ArrayList<>();
        setCommands(commands);
    }

    public CommandApi() {
    }

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

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender, @NotNull final Command bukkitCommand, @NotNull final String label, @NotNull final String[] args) {
        for (final de.finnp.simplecommandmanager.Command command: getCommands()) {
            for (final Method method: command.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(CommandHandler.class) &&
                        method.getParameterTypes().length != 1) continue;
                if (!method.getAnnotation(CommandHandler.class).value().equalsIgnoreCase(label)) continue;
                try {
                    if (method.isAnnotationPresent(CommandProperties.class)) {
                        final CommandProperties properties = method.getAnnotation(CommandProperties.class);
                        switch (properties.type()) {
                            case UNIVERSAL -> {
                                method.invoke(command, new CommandManager(commandSender, args, label));
                            }
                            case PLAYER -> {
                                if (commandSender instanceof Player)
                                    method.invoke(command, new CommandManager(commandSender, args, label));
                                else commandSender.sendMessage(properties.invalidSender());
                            }
                            case CONSOLE -> {
                                if (!(commandSender instanceof Player))
                                    method.invoke(command, new CommandManager(commandSender, args, label));
                                else commandSender.sendMessage(properties.invalidSender());
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + properties.type());
                        }
                    } else {
                        method.invoke(command, new CommandManager(commandSender, args, label));
                    }
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public List<@NotNull String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command bukkitCommand, @NotNull final String label, @NotNull final String[] args) {
        for (final de.finnp.simplecommandmanager.Command command: getCommands()) {
            for (final Method method: command.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(CommandHandler.class) &&
                        method.isAnnotationPresent(TabCompletion.class) &&
                        method.getParameterTypes().length != 1) continue;
                if (!method.getAnnotation(CommandHandler.class).value().equalsIgnoreCase(label)) continue;
                final TabCompletion completion = method.getAnnotation(TabCompletion.class);
                final de.finnp.simplecommandmanager.annotation.List[] lists = completion.value();
                int i = args.length;
                if (lists[i - 1] == null) continue;
                return Arrays.asList(lists[i - 1].value());
            }
        }
        return new ArrayList<>();
    }
}
