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

package space.quinoaa.spigotcommons.gui.frame;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import space.quinoaa.spigotcommons.data.Vector2i;

@Getter
public class ClickInfo {
	Vector2i slot;
	Player player;
	@Setter boolean cancelled;
	InventoryClickEvent event;

	void reset(Vector2i slot, Player player, InventoryClickEvent event){
		this.slot = slot;
		this.cancelled = true;
		this.player = player;
		this.event = event;
	}

	/**
	 * Events should only be read
	 */
	public InventoryClickEvent getEvent() {
		return event;
	}
}
