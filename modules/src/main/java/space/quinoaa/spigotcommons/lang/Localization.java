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

package space.quinoaa.spigotcommons.lang;

import lombok.extern.java.Log;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Log
public class Localization {
	final Class<?> pluginClass;
	Map<String, String> lang = new HashMap<>();



	public Localization(Object plugin, String languageFile, String... fallbacks) {
		this(plugin.getClass(), languageFile, fallbacks);
	}
	public Localization(Class<?> plugin, String languageFile, String... fallbacks) {
		this.pluginClass = plugin;

		loadMissingKeys(languageFile);
		for (String fallback : fallbacks) loadMissingKeys(fallback);
	}



	private void loadMissingKeys(String resource){
		InputStream input = pluginClass.getClassLoader().getResourceAsStream(resource);

		if(input == null) {
			log.warning(String.format("Missing language file: %s for plugin %s", resource, pluginClass.getCanonicalName()));
			return;
		}
		YamlConfiguration configuration = new YamlConfiguration();

		try {
			configuration.load(new InputStreamReader(input));
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException("Could not load language file", e);
		}

		loadMissingKeys(configuration);
	}

	/**
	 * Loads the keys inside the config that has not been loaded
	 */
	public void loadMissingKeys(YamlConfiguration config){
		config.getKeys(true).forEach(k->{
			if(config.isString(k) && !lang.containsKey(k)) lang.put(k, config.getString(k));
		});
	}

	/**
	 * Loads all the keys inside the config,
	 * overrides keys that are already loaded
	 */
	public void loadKeysOverride(YamlConfiguration config){
		config.getKeys(true).forEach(k->{
			if(config.isString(k)) lang.put(k, config.getString(k));
		});
	}




	public String get(String key){
		if(!lang.containsKey(key)) return "[Missing Language Key]";
		return lang.get(key);
	}

	public String format(String key, Object... values){
		return String.format(get(key), values);
	}
}
