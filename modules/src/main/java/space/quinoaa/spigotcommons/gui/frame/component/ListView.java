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

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.gui.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.ClickResult;
import space.quinoaa.spigotcommons.gui.frame.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListView<T> extends Component {
	final Function<T, ItemStack> mapper;
	private List<T> list = Collections.emptyList();

	@Getter private int page = 0, pageMax = 0;
	@Getter private int itemPerPage;

	private Consumer<T> clickHandler;

	public ListView(Function<T, ItemStack> mapper) {
		this.mapper = mapper;
	}

	public ListView(Function<T, ItemStack> mapper, List<T> list){
		this(mapper);
		this.list = list;
	}

	public ListView<T> setClickEvent(Consumer<T> handler){
		this.clickHandler = handler;
		return this;
	}


	@Override
	public void init() {
		itemPerPage = getSize().getArea();
		setList(list);
	}

	public void setList(List<T> list){
		this.list = list;
		pageMax = list.size() / itemPerPage;
		page = Math.min(page, pageMax);

		render();
	}

	@Override
	public void render() {
		int itemOffset = page * itemPerPage;
		int itemOffsetEnd = Math.min((page + 1) * itemPerPage, list.size());
		int width = getSize().x;

		fillItem(null, getSize());
		for (int i = itemOffset; i < itemOffsetEnd; i++) {
			setItem(mapper.apply(list.get(i)), Vector2i.fromSlotCount(i - itemOffset, width));
		}
	}

	@Override
	public void onClick(InventoryClickEvent event, Vector2i relative, ClickResult result) {
		if(clickHandler == null) return;

		int index = itemPerPage * page + relative.toIndex(getSize().x);
		if(index >= list.size()) return;

		clickHandler.accept(list.get(index));
	}

	/**
	 * Sets the current page.
	 */
	public void setPage(int page){
		this.page = Math.min(Math.max(page, 0), pageMax);

		render();
	}

	/**
	 * adds "add" to page index.
	 * @param add added to page index.
	 */
	public void movePage(int add) {
		setPage(page + add);
	}
}
