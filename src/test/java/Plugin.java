import de.finnp.simplecommandmanager.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final Command[] commands= {new PrintCommand()};
        new SimpleCommandManager(this, commands);
    }

}
