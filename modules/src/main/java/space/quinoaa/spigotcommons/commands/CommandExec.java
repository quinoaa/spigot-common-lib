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

package space.quinoaa.spigotcommons.commands;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import space.quinoaa.spigotcommons.commands.args.ArgumentParser;
import space.quinoaa.spigotcommons.commands.args.CommandArguments;
import space.quinoaa.spigotcommons.commands.args.ParsedArguments;
import space.quinoaa.spigotcommons.commands.impl.ParsedCommandHandler;
import space.quinoaa.spigotcommons.commands.impl.InvalidArgsHandler;
import space.quinoaa.spigotcommons.commands.impl.RawCommandHandler;
import space.quinoaa.spigotcommons.commands.impl.TabCompleteHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CommandExec implements Command {
	final RawCommandHandler handler;
	final TabCompleteHandler tabCompleter;



	public CommandExec(final ParsedCommandHandler handler,
					   final InvalidArgsHandler invalidArgs,
					   final ArgumentParser<?>... parsers) {
		this.handler = (sender, arguments) -> {
			int initial = arguments.getIndex();
			ParsedArguments parsed = new ParsedArguments(parsers, arguments);

			if(parsed.isBad()){
				arguments.setIndex(initial);
				invalidArgs.accept(sender, arguments);
			}else{
				handler.accept(sender, parsed);
			}
		};
		this.tabCompleter = (arguments) ->{
			for (ArgumentParser<?> parser : parsers) {
				if(arguments.getRemaining() == 1){
					String arg = arguments.getNext();

					return parser.getCompletions().stream()
							.filter(completion -> completion.startsWith(arg))
							.collect(Collectors.toList());
				}

				parser.consumeTabComplete(arguments);

			}

			return new ArrayList<>();
		};
	}


	@Override
	public void execute(CommandSender sender, CommandArguments arguments) {
		handler.accept(sender, arguments);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, CommandArguments arguments) {
		return tabCompleter.apply(arguments);
	}


}
