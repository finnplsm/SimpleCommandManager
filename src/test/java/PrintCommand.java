import de.finnp.simplecommandmanager.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PrintCommand implements Command{
    @CommandHandler("print")
    @CommandProperties(type = CommandProperties.CommandType.PLAYER, usage = "/print", aliases = {"pr", "printmessage"})
    private void onCommand(@NotNull final CommandManager commandManager){
        final String label=commandManager.getLabel();
        final String[] args=commandManager.getArguments();
        final CommandSender commandSender=commandManager.getSender();
        commandManager.setTabCompletion(args[0],"Hello");
        commandManager.setTabCompletion(args[1],"World!");
    }
}
