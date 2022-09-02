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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter @Setter @ToString
public class Vector2i {
	public static Vector2i ZERO = new Vector2i(0, 0);
	public final int x, y;


	public Vector2i add(Vector2i vec){
		return new Vector2i(x + vec.x, y + vec.y);
	}

	public void forEach(Consumer<Vector2i> consumer){
		for (int x = 0; x < this.x; x++) for (int y = 0; y < this.y; y++) {
			consumer.accept(new Vector2i(x, y));
		}
	}

	public int toIndex(int base) {
		return x + y * base;
	}

	public static Vector2i fromSlot(int slot, int base) {
		return new Vector2i(slot % base, slot / base);
	}

	public Bounds2i toBounds(int offsetx, int offsety) {
		return toBounds(new Vector2i(offsetx, offsety));
	}
	public Bounds2i toBounds(Vector2i offset) {
		return new Bounds2i(offset, this);
	}

	public int getArea() {
		return x * y;
	}

	public Vector2i subtract(Vector2i pos) {
		return new Vector2i(x - pos.x, y - pos.y);
	}
}
