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

package space.quinoaa.spigotcommons.commands.args.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.quinoaa.spigotcommons.commands.args.ArgumentParser;
import space.quinoaa.spigotcommons.commands.args.CommandArguments;
import space.quinoaa.spigotcommons.commands.args.ParseResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerParser extends ArgumentParser<Optional<Player>> {



	public PlayerParser(String key) {
		super(key);
	}

	@Override
	public ParseResult<Optional<Player>> parse(CommandArguments arguments) {
		return ParseResult.createResult(Optional.of(Bukkit.getPlayer(arguments.getNext())));
	}

	@Override
	public void consumeTabComplete(CommandArguments arguments) {
		arguments.getNext();
	}

	@Override
	public List<String> getCompletions() {
		return Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.collect(Collectors.toList());
	}
}
