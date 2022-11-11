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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.gui.data.Bounds2i;
import space.quinoaa.spigotcommons.gui.data.Vector2i;

public abstract class Component {
	@Getter Bounds2i bounds = null;
	@Getter ComponentHolder parent = null;
	@Getter int ZIndex = 0;


	public final Vector2i getSize(){
		return bounds.size;
	}

	public final void fillItem(ItemStack item, Bounds2i bounds){
		bounds.forEach(pos->setItem(item, pos));
	}

	public final void fillItem(ItemStack item, Vector2i bounds){
		bounds.forEach(pos->setItem(item, pos));
	}

	public final void setItem(ItemStack item, Vector2i relative){
		parent.setItem(item != null ? item.clone() : null, relative, this);
	}


	/**
	 * Added after the component is added to parents.
	 * Component is ready and can start rendering.
	 */
	public abstract void init();


	/**
	 * Callable after it is added to component holder.<br />
	 * <br />
	 * First called when {@link ComponentHolder#addComponent(Component, Bounds2i, int)} is used.
	 * Render with {@link #fillItem(ItemStack, Bounds2i)} and {@link #setItem(ItemStack, Vector2i)}
	 */
	public abstract void render();



	public abstract void onClick(InventoryClickEvent event, Vector2i relative, ClickResult result);

	public boolean supportsDrag(){
		return false;
	}
	public void onDrag(InventoryDragEvent event, Vector2i[] relative, ClickResult result){

	}
}
