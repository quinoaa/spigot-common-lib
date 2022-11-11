package space.quinoaa.testplugin;

import lombok.Getter;
import space.quinoaa.spigotcommons.ModulePlugin;
import space.quinoaa.spigotcommons.gui.GuiApi;
import space.quinoaa.spigotcommons.gui.GuiModule;
import space.quinoaa.spigotcommons.lang.Localization;
import space.quinoaa.testplugin.command.TestCommand;

public class TestPlugin extends ModulePlugin {
	public static TestPlugin instance;
	@Getter public static Localization lang;
	@Getter public static GuiApi guiApi;


	@Override
	public void enable() {
		lang = new Localization(this.getClass(), "en.yml");
		instance = this;
		guiApi = loadModule(GuiModule::new);

		TestCommand.register(this);
	}

	@Override
	public void disable() {

	}

}
