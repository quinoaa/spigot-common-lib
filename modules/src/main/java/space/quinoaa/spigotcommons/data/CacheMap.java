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

package space.quinoaa.spigotcommons.data;

import java.util.*;

@Deprecated
public class CacheMap<K, V> {
	final Map<K, V> map = new HashMap<>();
	final Object[] keys;
	int index = 0, capacity;

	public CacheMap(int cacheSize) {
		this.keys = new Object[cacheSize];
		this.capacity = cacheSize;
	}


	public void put(K key, V value){
		if(capacity == 0) return;

		if(!has(key)){
			Object prev = keys[index];
			if(prev != null)  map.remove(prev);
			keys[index] = value;

			index = (index + 1) % capacity;
		}
		map.put(key, value);
	}


	public boolean has(K key){
		return map.containsKey(key);
	}
	public V get(K key){
		return map.get(key);
	}

}
