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
import lombok.extern.java.Log;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.data.Bounds2i;
import space.quinoaa.spigotcommons.data.Vector2i;

@Getter @Log
public abstract class Component {
	private Bounds2i bounds;
	private int zIndex;
	private IFrame parent = null;

	public final void initData(Bounds2i bounds, int zIndex, IFrame frame){
		this.bounds = bounds;
		this.zIndex = zIndex;
		this.parent = frame;
	}




	public Vector2i getPos() {
		return getBounds().offset;
	}
	public final Vector2i getSize(){
		return bounds.size;
	}





	/**
	 * Draws an item to the position relative to this component.
	 * Warns if the item is out of bound
	 */
	public final void setItem(Vector2i position, ItemStack item){
		Vector2i abs = position.add(bounds.offset);
		if(parent.inventory == null){
			log.severe(this.getClass() + " attempted to render when not initialized");
			return;
		}
		parent.setItem(this, abs.toIndex(parent.getBase()), item);
	}

	/**
	 * Fills the bounds with items.
	 * Warns if the item is out of bound
	 */
	public final void fillItems(Bounds2i bounds, ItemStack item){
		bounds.forEach(pos->setItem(pos, item));
	}



	public void init(){}

	public void onTick(){}

	public void onDispose(){}

	public void onClick(ClickInfo info){}

	public abstract void render();
}
