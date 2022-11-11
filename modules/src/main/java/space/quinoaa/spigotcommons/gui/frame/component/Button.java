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

package space.quinoaa.spigotcommons.gui.frame.component;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.gui.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.ClickResult;
import space.quinoaa.spigotcommons.impl.Provider;

import java.util.function.Consumer;

public class Button extends Label {
	final Consumer<InventoryClickEvent> onClick;

	public Button(Provider<ItemStack> iconProvider, Consumer<InventoryClickEvent> onClick) {
		super(iconProvider);

		this.onClick = onClick;
	}

	public Button(ItemStack item, Consumer<InventoryClickEvent> onClick) {
		this(()->item, onClick);
	}


	@Override
	public void onClick(InventoryClickEvent event, Vector2i relative, ClickResult result) {
		onClick.accept(event);
	}
}
