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

package space.quinoaa.spigotcommons.data.keyvalue;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Deprecated
public interface Storage<TYPE> {
	void loadCopy(String key, Consumer<Optional<TYPE>> result, Consumer<Throwable> errorHandler, Consumer<Runnable> scheduler);

	default void loadCopy(String key, Consumer<Optional<TYPE>> result, Consumer<Throwable> errorHandler, JavaPlugin plugin){
		loadCopy(key, result, errorHandler, task->Bukkit.getScheduler().runTask(plugin, task));
	}


	void update(String key, Function<Optional<TYPE>, TYPE> valueHandler, Consumer<Throwable> errorHandler,
				Runnable onSuccess, Consumer<Runnable> scheduler);

	default void update(String key, Function<Optional<TYPE>, TYPE> valueHandler, Consumer<Throwable> errorHandler){
		update(key, valueHandler, errorHandler, null, null);
	}


	void remove(String key, Consumer<Throwable> errorHandler);

}
