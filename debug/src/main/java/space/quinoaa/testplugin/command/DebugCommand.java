package space.quinoaa.testplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.quinoaa.testplugin.TestPlugin;
import space.quinoaa.testplugin.gui.DebugMenu;

public class DebugCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		Player plr = (Player) commandSender;
		TestPlugin.guiModule.getApi().showGui(plr, new DebugMenu());

		return true;
	}
}
