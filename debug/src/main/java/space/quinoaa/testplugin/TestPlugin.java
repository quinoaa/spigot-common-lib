package space.quinoaa.testplugin;

import org.bukkit.plugin.java.JavaPlugin;
import space.quinoaa.spigotcommons.gui.GuiModule;
import space.quinoaa.testplugin.command.DebugCommand;

public class TestPlugin extends JavaPlugin {
	public static TestPlugin instance;
	public static GuiModule guiModule = new GuiModule();



	@Override
	public void onEnable() {
		instance = this;
		guiModule.load(this);

		getCommand("guidebug").setExecutor(new DebugCommand());
	}

	@Override
	public void onDisable() {
		guiModule.dispose();
	}
}
