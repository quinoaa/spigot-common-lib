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

package space.quinoaa.testplugin.gui.component;

import org.bukkit.Material;
import space.quinoaa.spigotcommons.gui.frame.ClickInfo;
import space.quinoaa.spigotcommons.gui.frame.Component;
import space.quinoaa.spigotcommons.util.ItemBuilder;

import java.util.Collections;

public class Counter extends Component {
	int count = 0;
	int clickCount = 0;

	@Override
	public void onTick() {
		count++;
		render();
	}

	@Override
	public void onClick(ClickInfo info) {
		if(info.getEvent().getCursor().getAmount() != 0) return;

		clickCount++;
		info.setCancelled(false);
	}

	@Override
	public void render() {
		fillItems(
				getSize().toBounds(0, 0),
				new ItemBuilder(Material.WATCH)
						.setName("Count: " + count / 20)
						.setLore(
								Collections.singletonList(
										"Given: " + clickCount
								)
						)
						.build()
		);
	}
}
