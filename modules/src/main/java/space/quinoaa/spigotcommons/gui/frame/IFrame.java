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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.data.Bounds2i;
import space.quinoaa.spigotcommons.data.Vector2i;
import space.quinoaa.spigotcommons.gui.GuiApi;
import space.quinoaa.spigotcommons.gui.GuiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class IFrame implements GuiHandler {
	static final int INVENTORY_WIDTH = 9;
	@Getter private GuiApi guiApi = null;

	final Function<Player, Inventory> inventoryBuilder;
	Inventory inventory = null;
	@Getter Player player;


	final List<Component> components = new ArrayList<>();
	final Map<Integer, Component> slotMask = new HashMap<>();


	/**
	 * @param inventoryBuilder should create an empty inventory
	 */
	public IFrame(Function<Player, Inventory> inventoryBuilder) {
		this.inventoryBuilder = inventoryBuilder;
	}

	public IFrame(int size, String title){
		this(plr->Bukkit.createInventory(plr, size, title));
	}



	public int getBase(){
		return INVENTORY_WIDTH;
	}


	/**
	 * Adds a component with 1x1 size.
	 */
	public void addComponent(Component component, Vector2i position){
		addComponent(component, new Bounds2i(position, new Vector2i(1, 1)), 0);
	}
	/**
	 * Adds a component with 1x1 size.
	 */
	public void addComponent(Component component, Vector2i position, int zIndex){
		addComponent(component, new Bounds2i(position, new Vector2i(1, 1)), zIndex);
	}
	public void addComponent(Component component, Bounds2i bounds){
		addComponent(component, bounds, 0);
	}
	public void addComponent(Component component, Bounds2i bounds, int zIndex){
		components.add(component);
		bounds.forEach(pos -> {
			int index = pos.toIndex(INVENTORY_WIDTH);
			Component original = slotMask.get(index);

			if(original == null || zIndex >= original.getZIndex()) {
				if(inventory != null) inventory.setItem(index, null);
				slotMask.put(index, component);
			}
		});
		component.init(bounds, zIndex, this);
		if(inventory != null) component.render();
	}

	public void setItem(Component component, int slot, ItemStack item){
		if(slot < 0 || slot >= inventory.getSize()) return;
		if(slotMask.get(slot) == component) inventory.setItem(slot, item);
	}




	@Override
	public Inventory getInventory(Player player, GuiApi api) {
		if(inventory != null) throw new IllegalStateException("Inventory has already been created");

		inventory = inventoryBuilder.apply(player);
		this.player = player;
		this.guiApi = api;
		components.forEach(Component::render);

		return inventory;
	}


	@Override
	public void onClose(Player player) {
		components.forEach(Component::onDispose);
		this.player = null;
		this.inventory = null;
	}

	@Override
	public void tick(Player player) {
		components.forEach(Component::onTick);
	}

	ClickInfo clickInfo = new ClickInfo();
	@Override
	public void click(InventoryClickEvent event) {
		if(event.getClickedInventory() != event.getView().getTopInventory()) return;

		Component component = slotMask.get(event.getSlot());
		if(component == null) return;

		Vector2i pos = Vector2i.fromSlot(event.getSlot(), INVENTORY_WIDTH);
		clickInfo.reset(pos, (Player) event.getWhoClicked(), event);

		component.onClick(clickInfo);

		if(clickInfo.cancelled) event.setCancelled(true);
	}

	@Override
	public boolean isShared() {
		return false;
	}
}
