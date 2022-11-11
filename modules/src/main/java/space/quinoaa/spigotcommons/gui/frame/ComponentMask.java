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

import space.quinoaa.spigotcommons.gui.data.Bounds2i;
import space.quinoaa.spigotcommons.gui.data.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ComponentMask {
	final List<Component> components = new ArrayList<>();
	final Component[] mask;
	final Vector2i size;

	public ComponentMask(Vector2i size) {
		this.size = size;
		this.mask = new Component[size.getArea()];
	}

	public boolean isComponent(Component component, Vector2i position){
		if(!position.isInside(size)) return false;
		return mask[position.toIndex(size.x)] == component;
	}

	public Component getComponent(Vector2i position){
		return mask[position.toIndex(size.x)];
	}

	public void addComponent(Component component, Bounds2i bounds, int ZIndex){
		component.bounds = bounds;
		component.ZIndex = ZIndex;

		components.add(component);
		bounds.forEach(pos->{
			int index = pos.toIndex(size.x);

			if(mask[index]==null || mask[index].getZIndex()<ZIndex) mask[index] = component;
		});
	}

	public void reprocessZIndex(Vector2i position){
		if(!position.isInside(size)) return;

		int index = position.toIndex(size.x);
		mask[index] = null;
		for (Component component : components) {
			mask[index] = component;
		}
	}

	public void removeComponent(Component component){
		if(!components.remove(component)) return;

		component.bounds.forEach(this::reprocessZIndex);
	}
}
