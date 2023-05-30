package de.finnp.simplecommandmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final Command[] commands= {new TestCommand()};
        new SimpleCommandManager(this, commands);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
}
