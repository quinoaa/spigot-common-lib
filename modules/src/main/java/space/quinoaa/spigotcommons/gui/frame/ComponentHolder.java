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

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.gui.data.Bounds2i;
import space.quinoaa.spigotcommons.gui.data.Vector2i;

public abstract class ComponentHolder extends ComponentMask{


	public ComponentHolder(Vector2i size) {
		super(size);
	}

	public void setItem(ItemStack item, Vector2i componentRelative, Component component){
		if(!componentRelative.isInside(component.bounds.size)) return;

		Vector2i absolute = componentRelative.add(component.bounds.offset);
		if(mask[absolute.toIndex(size.x)] != component) return;

		implSetItem(item, absolute);
	}


	@Override
	public void addComponent(Component component, Bounds2i bounds, int ZIndex) {
		if(component.parent != null) throw new IllegalStateException("Component already has a parent.");

		super.addComponent(component, bounds, ZIndex);
		component.parent = this;

		component.init();
		component.render();
	}

	public void addComponent(Component component, Vector2i position, int ZIndex) {
		addComponent(component, new Bounds2i(position, new Vector2i(1, 1)), ZIndex);
	}

	/**
	 * removes the component
	 * @param component to remove
	 */
	public void removeComponent(Component component){
		super.removeComponent(component);
		component.parent = null;
		component.bounds.forEach(this::clearItem);
		component.bounds = null;
	}

	/**
	 * Re-renders all components
	 */
	public void render(){
		components.forEach(Component::render);
	}


	public void handleClick(InventoryClickEvent event, Vector2i relative, ClickResult result){
		Component c = mask[relative.toIndex(size.x)];

		if(c != null) c.onClick(event, relative.subtract(c.bounds.offset), result);
	}
	public void handleDrag(InventoryDragEvent event, Vector2i[] relative, ClickResult result){
		if(relative.length == 0) return;
		result.cancelled = true;

		/*
		Component c = null;
		for (Vector2i pos : relative) {
			Component atPos = mask[pos.toIndex(size.x)];

			if(c == null) c = atPos;
			else if(c != atPos) return;
		}

		if(c == null) return;
		Vector2i pos = c.bounds.offset;
		c.onDrag(event,
				Arrays.stream(relative).map(v->v.subtract(pos)).toArray(Vector2i[]::new),
				result);
		 */
	}

	public boolean hasDragSupportedComponent(){
		return false;
		/*
		for (Component component : components) if(component.supportsDrag()) return true;
		return false;
		 */
	}



	public abstract void implSetItem(ItemStack item, Vector2i position);

	/**
	 * Can be used to set background.
	 * @param position position to reset
	 */
	public abstract void clearItem(Vector2i position);
}
