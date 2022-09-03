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
import lombok.Setter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import space.quinoaa.spigotcommons.data.Provider;
import space.quinoaa.spigotcommons.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.ClickInfo;
import space.quinoaa.spigotcommons.gui.frame.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListView<T> extends Component {
	List<T> list;
	Function<T, ItemStack> mapper;
	Provider<ItemStack> background = ()->null;
	BiConsumer<T, ClickType> onClick;
	@Getter @Setter	Consumer<ListView<T>> pageListener = (list)->{};

	@Getter int itemPerPage = 0;
	@Getter int page = 0;
	@Getter int pageMax = 0;

	public ListView(List<T> list, Function<T, ItemStack> mapper, BiConsumer<T, ClickType> onClick) {
		setList(list);
		this.mapper = mapper;
		this.onClick = onClick;
	}

	public ListView(List<T> list, Function<T, ItemStack> mapper, BiConsumer<T, ClickType> onClick,
					Button previous, Button next) {
		this(list, mapper, onClick);
		previous.setOnClick(t->turnPages(-1));
		next.setOnClick(t->turnPages(1));
	}


	@Override
	public void init() {
		itemPerPage = getSize().getArea();
		updateList();
	}



	public void setPage(int page){
		this.page = page;
		updateList();
	}
	public void turnPages(int pages){
		this.page = this.page + pages;
		updateList();
	}


	public void setList(List<T> list){
		this.list = new ArrayList<>(list);
		updateList();
	}

	public void updateList(){
		if(itemPerPage == 0) return;

		pageMax = list.size() / itemPerPage;
		page = Math.min(page, pageMax);
		page = Math.max(0, page);

		render();
		pageListener.accept(this);
	}


	@Override
	public void onClick(ClickInfo info) {
		if(itemPerPage == 0) return;

		int index = info.getSlot().toIndex(getSize().x) + page * itemPerPage;
		if(index < list.size()){
			onClick.accept(list.get(index), info.getEvent().getClick());
		}
	}



	@Override
	public void render() {
		if(itemPerPage == 0) return;

		fillItems(getSize().toBounds(0, 0), background.get());

		int pageOffset = page * itemPerPage, max = Math.min(itemPerPage + pageOffset, list.size());
		for (int i = pageOffset; i < max; i++) {
			T item = list.get(i);

			Vector2i position = Vector2i.fromSlot(i - pageOffset, getSize().x);
			setItem(position, mapper.apply(item));
		}
	}
}
