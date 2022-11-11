/*
 * MIT License
 *
 * Copyright (c) 2022 quinoaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package space.quinoaa.testplugin.command;


import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import space.quinoaa.spigotcommons.commands.CommandExec;
import space.quinoaa.spigotcommons.commands.CommandNode;
import space.quinoaa.spigotcommons.commands.args.type.NumberParser;
import space.quinoaa.spigotcommons.commands.args.type.PlayerParser;
import space.quinoaa.spigotcommons.commands.args.type.WordParser;
import space.quinoaa.testplugin.TestPlugin;
import space.quinoaa.testplugin.gui.DebugGui;

import java.util.Collections;
import java.util.Optional;

public class TestCommand  {
	public static void register(JavaPlugin plugin){
		CommandNode node = new CommandNode((sender, args)->{});

		node.addNode("lol", new CommandExec(
				(sender, args)->{
					Optional<Player> plr = args.getArgument("plr", PlayerParser.class);

					if(!plr.isPresent()){
						sender.sendMessage("no player found");
						return;
					}

					for (int i = 0; i < args.getArgument("count", NumberParser.Int.class); i++) {
						plr.get().sendMessage(args.getArgument("txt", WordParser.class));
					}
				},
				(sender, args)->{

				},
				new PlayerParser("plr"),
				new WordParser("txt"),
				new NumberParser.Int("count")
		));

		node.createExec("localization",
				(sender, args)->{
					sender.sendMessage("should be test: " + TestPlugin.getLang().get("ab.cd"));
					sender.sendMessage("should give error: " + TestPlugin.getLang().get("swfsdfF sdf sdf "));
				}, (sender, args)->{

				});


		node.createExec("gui",
				((sender, arguments) -> {
					if(sender instanceof Player) DebugGui.open((Player)sender);
				}), args-> Collections.emptyList());




		node.register(plugin, "test");
	}
}
