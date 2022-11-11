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

package space.quinoaa.spigotcommons.gui.internal;

import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import space.quinoaa.spigotcommons.gui.GuiHandler;
import space.quinoaa.spigotcommons.gui.GuiModule;

import java.util.HashMap;
import java.util.Map;

@Log
public class GuiManager {
	final GuiModule module;
	final JavaPlugin plugin;
	Map<Player, GuiInfo> openGuis = new HashMap<>();
	Map<Player, GuiHandler.Ticker> tickers = new HashMap<>();

	public GuiManager(GuiModule module, JavaPlugin plugin) {
		this.module = module;
		this.plugin = plugin;
	}


	public void showGui(Player player, GuiHandler handler) {
		Bukkit.getScheduler().runTask(plugin, ()->{ // or else inventory will be de-sync
			Inventory inv = handler.setup(player, module.getApi());
			InventoryView view = player.openInventory(inv);
			openGuis.put(player, new GuiInfo(view, handler));

			handler.tick(player).ifPresent(value -> tickers.put(player, value));
		});
	}

	public void onClose(Player player, InventoryView view){

		GuiInfo info = openGuis.remove(player);
		if(info == null || info.inventory != view) return;

		tickers.remove(player);
		openGuis.remove(player);

		try{
			info.gui.onClose(player);
		}catch (Exception e){
			e.printStackTrace();
			log.warning("Could not close gui for: " + player.getName());
		}
		info.inventory.getTopInventory().clear();
	}

	public void tick() {
		tickers.forEach((p,t)->{
			try{
				t.tick();
			}catch (Exception e){
				e.printStackTrace();
				log.warning("Could not tick inventory for player: " + p.getName());
			}
		});
	}

	public GuiHandler getGui(Player player) {
		return openGuis.containsKey(player) ? openGuis.get(player).gui : null;
	}
}
