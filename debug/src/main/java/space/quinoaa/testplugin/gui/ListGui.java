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
import space.quinoaa.spigotcommons.gui.frame.component.Label;
import space.quinoaa.spigotcommons.gui.frame.component.ListView;
import space.quinoaa.spigotcommons.util.ItemBuilder;
import space.quinoaa.testplugin.TestPlugin;

import java.util.ArrayList;
import java.util.List;

public class ListGui extends CFrame {
	Runnable onClose;
	public ListGui(Runnable onClose, Player player) {
		super(9 * 6 /* don't invert lol */, "List Test");

		this.onClose = onClose;

		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 2000; i++) {
			list.add(i);
		}

		ListView<Integer> lv = new ListView<Integer>(
				i-> ItemBuilder.of(Material.PAPER)
						.setName("No " + i)
						.build(),
				list
		).setClickEvent(num->player.sendMessage("number: " + num));

		addComponent(new Label(ItemBuilder.of(Material.STAINED_GLASS_PANE).build()),
				new Bounds2i(0, 0, 9, 6), 0);

		addComponent(lv, new Bounds2i(0, 0, 9, 5), 1);




		addComponent(new Button(ItemBuilder.of(Material.BOOK).setName("Previous").build(), click->{
			lv.movePage(-1);
			render();
		}), new Vector2i(3, 5), 1);

		addComponent(new Label(
				()->ItemBuilder.of(Material.MAP).setName((lv.getPage() + 1) + "/" + lv.getPageMax()).build()),
				new Vector2i(4, 5), 1);

		addComponent(new Button(ItemBuilder.of(Material.BOOK).setName("Next").build(), click->{
			lv.movePage(1);
			render();
		}), new Vector2i(5, 5), 1);

		addComponent(new Button(ItemBuilder.of(Material.NOTE_BLOCK).setName("Randomize").build(),click->{
			for (int i = 0; i < 1000; i++) {
				list.add((int) (Math.random() * (list.size() - 1)), list.remove(0));
			}
			lv.setList(list);
		}), new Vector2i(0, 5), 1);



		addComponent(new Label(ItemBuilder.of(Material.STAINED_GLASS_PANE).setDurability((byte)15).build()),
				new Bounds2i(0, 0, 9, 6), 0);
	}

	@Override
	public void onClose(Player player) {
		onClose.run();
	}

	public static void open(Player player, Runnable onClose) {
		TestPlugin.getGuiApi().showGui(player, new ListGui(onClose, player));
	}
}
