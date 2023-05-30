package de.finnp.simplecommandmanager;

import de.finnp.simplecommandmanager.annotation.CommandHandler;
import de.finnp.simplecommandmanager.annotation.CommandProperties;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public class SimpleCommandManager {
    private Plugin plugin;
    private CommandApi commandApi;
    private Command[] commands;

    public SimpleCommandManager(@NotNull final Plugin plugin, @NotNull final Command[] commands) {
        setPlugin(plugin);
        setCommands(commands);
        setCommandApi(new CommandApi(getCommands()));
        registerCommands();
        plugin.getLogger().log(Level.INFO,"Commands successfully integrated into the SimpleCommandManager");
    }

    @NotNull
    private Plugin getPlugin() {
        return plugin;
    }

    private void setPlugin(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    private CommandApi getCommandApi() {
        return commandApi;
    }

    private void setCommandApi(@NotNull final CommandApi commandApi) {
        this.commandApi = commandApi;
    }

    @NotNull
    private Command[] getCommands() {
        return commands;
    }

    private void setCommands(@NotNull final Command[] commands) {
        this.commands = commands;
    }

    private void registerCommands(){
        for (final Command command : getCommands()){
            for (final Method method : command.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(CommandHandler.class) ||
                        method.getParameterTypes().length != 1) continue;
                getCommandApi().registerCommandHandler(command.getClass());
                PluginCommand pluginCommand=Objects.requireNonNull(getPlugin().getServer().getPluginCommand(method.getAnnotation(CommandHandler.class).value()));
                pluginCommand.setExecutor(getCommandApi());
                pluginCommand.setTabCompleter(getCommandApi());
                if (method.isAnnotationPresent(CommandProperties.class)){
                    pluginCommand.setUsage(method.getAnnotation(CommandProperties.class).usage().replaceAll("%label%",method.getAnnotation(CommandHandler.class).value()));
                    pluginCommand.setPermission(method.getAnnotation(CommandProperties.class).permission());
                    pluginCommand.setAliases(Arrays.asList(method.getAnnotation(CommandProperties.class).aliases()));
                    pluginCommand.setDescription(method.getAnnotation(CommandProperties.class).description());
                    pluginCommand.setPermissionMessage(method.getAnnotation(CommandProperties.class).permissionMessage());
                }
            }
        }
    }
}
