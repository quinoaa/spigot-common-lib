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

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import space.quinoaa.spigotcommons.commands.args.ArgumentParser;
import space.quinoaa.spigotcommons.commands.args.CommandArguments;
import space.quinoaa.spigotcommons.commands.impl.ParsedCommandHandler;
import space.quinoaa.spigotcommons.commands.impl.InvalidArgsHandler;
import space.quinoaa.spigotcommons.commands.impl.RawCommandHandler;
import space.quinoaa.spigotcommons.commands.impl.TabCompleteHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CommandNode implements Command, CommandExecutor, TabCompleter {
	private final Map<String, Command> nodes = new HashMap<>();

	private final BiConsumer<CommandSender, CommandArguments> noArgsExecutor;
	private final BiConsumer<CommandSender, CommandArguments> fallbackExecutor;


	/**
	 * Create a new command node. It still has to be registered. {@link #register(JavaPlugin, String)}
	 * @param fallback executed when no arguments are used
	 */
	public CommandNode(BiConsumer<CommandSender, CommandArguments> fallback) {
		this(fallback, fallback);
	}

	/**
	 * Create a new command node. It still has to be registered. {@link #register(JavaPlugin, String)}
	 * @param noArgsExecutor executed when command node is executed without arguments
	 * @param fallbackExecutor executed when unknown subcommand is run.
	 */
	public CommandNode(BiConsumer<CommandSender, CommandArguments> noArgsExecutor,
					   BiConsumer<CommandSender, CommandArguments> fallbackExecutor) {
		this.noArgsExecutor = noArgsExecutor;
		this.fallbackExecutor = fallbackExecutor;
	}




	/**
	 * Registers the command and the tab completer
	 */
	public void register(JavaPlugin plugin, String commandName){
		PluginCommand cmd = plugin.getCommand(commandName);
		cmd.setTabCompleter(this);
		cmd.setExecutor(this);
	}




	/**
	 * Registers the command inside this node
	 */
	public void addNode(String name, Command command){
		nodes.put(name, command);
	}

	/**
	 * Creates and registers a new {@link CommandNode} inside this node.
	 */
	public CommandNode createNode(String name, BiConsumer<CommandSender, CommandArguments> noArgsExecutor,
								  BiConsumer<CommandSender, CommandArguments> fallbackExecutor){
		CommandNode node = new CommandNode(noArgsExecutor, fallbackExecutor);
		addNode(name, node);
		return node;
	}

	/**
	 * Creates and registers a new {@link CommandExec} and registers it.
	 * @return self
	 */
	public CommandNode createExec(String name,
								  final ParsedCommandHandler handler,
								  final InvalidArgsHandler invalidArgs,
								  final ArgumentParser<?>... parsers){
		CommandExec exec = new CommandExec(handler, invalidArgs, parsers);
		addNode(name, exec);
		return this;
	}

	/**
	 * Creates and registers a new {@link CommandExec} and registers it.
	 * @return self
	 */
	public CommandNode createExec(String name,
								  final RawCommandHandler handler,
								  final TabCompleteHandler tabComplete){
		CommandExec exec = new CommandExec(handler, tabComplete);
		addNode(name, exec);
		return this;
	}




	@Override
	public void execute(CommandSender sender, CommandArguments arguments) {
		String node = arguments.getNext();

		if(node == null){
			arguments.previous();
			noArgsExecutor.accept(sender, arguments);
		}else if(!nodes.containsKey(node)){
			arguments.previous();
			fallbackExecutor.accept(sender, arguments);
		}else{
			nodes.get(node).execute(sender, arguments);
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, CommandArguments arguments) {
		String current = arguments.getNext();

		if(!arguments.hasNext()){
			return nodes.keySet().stream()
					.filter(key->key.startsWith(current))
					.collect(Collectors.toList());
		}

		Command node = nodes.get(current);
		if(node != null) return node.tabComplete(sender, arguments);

		return Collections.emptyList();
	}




	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		execute(sender, new CommandArguments(args));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
		return tabComplete(sender, new CommandArguments(args));
	}
}
