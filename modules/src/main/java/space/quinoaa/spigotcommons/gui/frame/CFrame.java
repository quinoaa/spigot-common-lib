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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.gui.GuiApi;
import space.quinoaa.spigotcommons.gui.GuiHandler;
import space.quinoaa.spigotcommons.gui.data.Vector2i;

import java.util.Optional;

public class CFrame extends ComponentHolder implements GuiHandler {
	final int inventoryWidth = Vector2i.CHEST_WIDTH;
	final Inventory inventory;

	public CFrame(int size, String title) {
		this(Bukkit.createInventory(null, size, title));
	}

	public CFrame(Inventory inventory) {
		super(Vector2i.resolveRectHeight(inventory.getSize(), Vector2i.CHEST_WIDTH));
		this.inventory = inventory;
	}


	@Override
	public Inventory setup(Player player, GuiApi api) {
		return inventory;
	}

	@Override
	public void onClose(Player player) {

	}

	@Override
	public Optional<Ticker> tick(Player player) {
		return Optional.empty();
	}

	@Override
	public void click(InventoryClickEvent event) {
		if(event.getClickedInventory() != event.getView().getTopInventory()) return;
		ClickResult result = new ClickResult();

		try{
			handleClick(event, Vector2i.fromSlotCount(event.getSlot(), inventoryWidth), result);
		}catch (Exception e){
			event.setCancelled(true);
			e.printStackTrace();
			return;
		}

		event.setCancelled(result.isCancelled());
	}

	@Override
	public void drag(InventoryDragEvent event) {
		ClickResult result = new ClickResult();

		handleDrag(event,
				event.getInventorySlots().stream()
				.map(slot->Vector2i.fromSlotCount(slot, inventoryWidth))
				.toArray(Vector2i[]::new),
				result);

		event.setCancelled(result.isCancelled());
	}

	@Override
	public void implSetItem(ItemStack item, Vector2i position) {
		inventory.setItem(position.toIndex(inventoryWidth), item);
	}

	@Override
	public void clearItem(Vector2i position) {
		inventory.setItem(position.toIndex(inventoryWidth), null);
	}
}
