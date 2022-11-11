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

package space.quinoaa.spigotcommons;

import org.bukkit.plugin.java.JavaPlugin;
import space.quinoaa.spigotcommons.impl.Builder;

import java.util.ArrayList;
import java.util.List;

public abstract class ModulePlugin extends JavaPlugin {
	private final List<Module<?>> modules = new ArrayList<>();

	/**
	 * Loads the module
	 * @param moduleConstructor ex: "GuiModule::new"
	 * @return Module api
	 */
	public final <API> API loadModule(Builder<Module<API>> moduleConstructor){
		Module<API> mod = moduleConstructor.build();

		mod.load(this);
		modules.add(mod);

		return mod.getApi();
	}


	public abstract void enable();
	public abstract void disable();


	@Override
	public final void onEnable() {
		enable();
	}

	@Override
	public final void onDisable() {
		try{
			disable();
		}catch (RuntimeException e){
			e.printStackTrace();
		}

		while(!modules.isEmpty()){
			try{
				modules.remove(0).dispose();
			}catch (RuntimeException e){
				e.printStackTrace();
			}
		}
	}
}
