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
import space.quinoaa.spigotcommons.data.Bounds2i;
import space.quinoaa.spigotcommons.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.IFrame;
import space.quinoaa.spigotcommons.gui.frame.component.Button;
import space.quinoaa.spigotcommons.gui.frame.component.ListView;
import space.quinoaa.spigotcommons.gui.frame.component.Panel;
import space.quinoaa.spigotcommons.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListTest extends IFrame {

	Button previous = new Button(
			new ItemBuilder(Material.BOOK).setName("Previous").build()
	);

	Button next = new Button(
			new ItemBuilder(Material.BOOK).setName("Next").build()
	);

	ListView<Integer> list = new ListView<>(
			new ArrayList<>(),
			i->new ItemBuilder(Material.PAPER).setName("paper" + i).build(),
			(i, click) -> getPlayer().sendMessage("clicked " + i + " type " + click),
			previous,
			next
	);

	public ListTest() {
		super(9 * 6, "List test");

		addComponent(list, new Bounds2i(0, 0, 9, 5));
		addComponent(
				new Panel(new ItemBuilder(Material.STAINED_GLASS_PANE).build()),
				new Bounds2i(0, 5, 9, 1),
				-1
		);
		addComponent(previous, new Vector2i(3, 5));
		addComponent(next, new Vector2i(5, 5));



		list.setList(genList());
	}
	
	private List<Integer> genList(){
		List<Integer> ls = new ArrayList<>();

		for (int i = 0; i < 1000; i++) {
			ls.add(i);
		}
		
		return ls;
	}


}
