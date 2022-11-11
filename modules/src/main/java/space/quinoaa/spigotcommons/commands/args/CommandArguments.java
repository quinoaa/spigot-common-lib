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

package space.quinoaa.spigotcommons.commands.args;

import lombok.Getter;

public class CommandArguments {
	@Getter final String[] originalArguments;
	private int index = -1;

	public CommandArguments(String... args) {
		this.originalArguments = args;
	}


	public int getIndex(){
		return index;
	}

	public void setIndex(int index){
		if(index < -1) index = -1;
		this.index = index;
	}




	public int getRemaining(){
		return originalArguments.length - index - 1;
	}



	public String getNext(){
		index++;
		return index < originalArguments.length ? originalArguments[index] : null;
	}

	public void previous(){
		index--;
		if(index < -1) index = -1;
	}

	public boolean hasNext(){
		return index + 1 < originalArguments.length;
	}
}
