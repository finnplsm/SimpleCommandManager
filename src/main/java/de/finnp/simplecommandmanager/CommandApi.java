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
    private final Map<@NotNull String, @NotNull List<@NotNull String>> tabCompletions;
    private final List<@NotNull Object> commandHandlers;

    public CommandApi() {
        tabCompletions = new HashMap<>();
        commandHandlers = new ArrayList<>();
    }

    public void registerCommandHandler(@NotNull final Object commandHandler) {
        commandHandlers.add(commandHandler);
    }

    public void addTabCompletion(@NotNull final String argument, @NotNull final List<String> completions) {
        tabCompletions.put(argument, completions);
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        for (Object commandHandler : commandHandlers) {
            commandSender.sendMessage("1");
            for (Method method : commandHandler.getClass().getDeclaredMethods()) {
                commandSender.sendMessage("2");
                if (method.isAnnotationPresent(CommandHandler.class)) {
                    commandSender.sendMessage("3");
                    final CommandHandler annotation = method.getAnnotation(CommandHandler.class);
                    if (annotation.value().equalsIgnoreCase(command.getName())) {
                        commandSender.sendMessage("4");
                        try {
                            if(method.isAnnotationPresent(CommandProperties.class)){
                                commandSender.sendMessage("5");
                                final CommandProperties commandProperties = method.getAnnotation(CommandProperties.class);
                                if(commandProperties.type()== CommandProperties.CommandType.PLAYER&&commandSender instanceof Player){
                                    commandSender.sendMessage("6");
                                    method.invoke(commandHandler, new CommandManager(commandSender, args, label, tabCompletions));
                                }else if(commandProperties.type() == CommandProperties.CommandType.CONSOLE && !(commandSender instanceof Player)){
                                    commandSender.sendMessage("7");
                                    method.invoke(commandHandler, new CommandManager(commandSender, args, label, tabCompletions));
                                }else if(commandProperties.type()== CommandProperties.CommandType.UNIVERSAL){
                                    commandSender.sendMessage("8");
                                    method.invoke(commandHandler, new CommandManager(commandSender, args, label, tabCompletions));
                                }
                            }else {
                                commandSender.sendMessage("9");
                                method.invoke(commandHandler, new CommandManager(commandSender, args, label, tabCompletions));
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length > 1) {
            final List<String> completions = tabCompletions.get(args[0]);
            if (completions != null) {
                return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
            }
        }
        return null;
    }
}
