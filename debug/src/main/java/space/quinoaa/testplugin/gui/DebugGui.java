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

package space.quinoaa.testplugin.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import space.quinoaa.spigotcommons.gui.data.Bounds2i;
import space.quinoaa.spigotcommons.gui.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.CFrame;
import space.quinoaa.spigotcommons.gui.frame.component.Button;
import space.quinoaa.spigotcommons.util.ItemBuilder;
import space.quinoaa.testplugin.TestPlugin;

public class DebugGui {

	public static void open(Player player) {
		CFrame frame = new CFrame(27, "debug gui");

		frame.addComponent(
				new Button(
				new ItemBuilder(Material.DIAMOND_SWORD)
				.setName("lol").build(),
				click->{
					ListGui.open(player, ()->open(player));
				}),
				new Vector2i(1, 1),
				1
		);

		TestPlugin.getGuiApi().showGui(player, frame);
	}
}
