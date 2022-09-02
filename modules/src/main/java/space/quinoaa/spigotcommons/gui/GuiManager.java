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

package space.quinoaa.spigotcommons.gui;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GuiManager {
	private final GuiApi api;
	private final Map<Player, Context> contexes = new HashMap<>();

	public GuiManager(GuiApi api) {
		this.api = api;
	}

	public void showGui(Player player, GuiHandler handler){
		closeGui(player);
		Inventory inventory = handler.getInventory(player, api);
		if(inventory == null) throw new IllegalStateException("GuiHandler gave null inventory");

		contexes.put(player, new Context(handler, inventory));

		player.openInventory(inventory);
	}


	public GuiHandler getGui(Player player) {
		Context context = contexes.get(player);
		if(context == null) return null;

		return context.handler;
	}




	public void click(InventoryClickEvent event, Player entity) {
		Context ctx = contexes.get(entity);

		if(ctx != null) ctx.handler.click(event);
	}



	public void closeGui(Player player){
		player.closeInventory();
		guiClosed(player);
	}


	public void guiClosed(Player player){
		Context ctx = contexes.remove(player);

		if(ctx == null) return;
		if(!ctx.handler.isShared()) ctx.inventory.clear(); // Just in case

		ctx.handler.onClose(player);
	}

	public void dispose(){
		Set<Player> open = new HashSet<>(contexes.keySet());

		open.forEach(this::closeGui);
	}

	public void tick() {
		contexes.forEach((plr, ctx) -> {
			try {
				ctx.handler.tick(plr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	@AllArgsConstructor
	private static class Context{
		final GuiHandler handler;
		final Inventory inventory;
	}
}
