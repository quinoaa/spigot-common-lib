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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ParsedArguments {
	Map<String, Object> stored = new HashMap<>();

	@Getter boolean bad;

	public ParsedArguments(ArgumentParser<?>[] parsers, CommandArguments arguments) {
		for (ArgumentParser<?> parser : parsers) {
			if(!arguments.hasNext()){
				bad = true;
				return;
			}
			ParseResult<?> result = parser.parse(arguments);

			if(result.isBad()){
				bad = true;
				return;
			}else{
				stored.put(parser.key, result.value);
			}
		}

		if(arguments.hasNext()) bad = true;
	}

	@SuppressWarnings("unchecked")
	public <T> T getArgument(String key, Class<? extends ArgumentParser<T>> clazz){
		return (T) stored.get(key);
	}



}
