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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

@Deprecated
public class UnsafeFSStore<TYPE> implements Storage<TYPE> {
	private final Path directory;
	private final Function<TYPE, byte[]> serializer;
	private final Function<byte[], TYPE> deserializer;
	private final Function<TYPE, TYPE> duplicate;

	private final ExecutorService sync = Executors.newSingleThreadExecutor();



	public UnsafeFSStore(Path directory, Function<TYPE, byte[]> serializer, Function<byte[], TYPE> deserializer,
						 Function<TYPE, TYPE> duplicate,
						 int cacheSize) throws IOException {
		Files.createDirectories(directory);
		this.directory = directory;
		this.serializer = serializer;
		this.deserializer = deserializer;
		this.duplicate = duplicate;
	}

	public UnsafeFSStore(Path directory, Serializer<TYPE> serializer, int cacheSize) throws IOException {
		this(directory, serializer.serializer, serializer.deserializer, serializer.duplicate, cacheSize);
	}

	private Path getFile(String key){
		return directory.resolve(key);
	}

	private byte[] loadByteFromFile(String key) throws IOException {
		Path file = getFile(key);

		if(!Files.exists(file) || Files.size(file) == 0) return null;

		return Files.readAllBytes(file);
	}


	private void remove(String key) throws IOException {
		Path file = getFile(key);

		if(Files.exists(file)) Files.delete(file);
	}
	private TYPE load(String key) throws IOException {
		byte[] data = loadByteFromFile(key);
		if(data == null) return null;
		return deserializer.apply(data);
	}
	private void save(String key, TYPE value) throws IOException {
		Path file = getFile(key);

		Files.write(file, serializer.apply(value));
	}



	@Override
	public void loadCopy(String key, Consumer<Optional<TYPE>> result, Consumer<Throwable> errorHandler,
						 Consumer<Runnable> scheduler) {
		sync.submit(()->{
			try {
				TYPE original = load(key);
				TYPE send = original == null ? null : duplicate.apply(original);
				scheduler.accept(()->result.accept(Optional.ofNullable(send)));
			} catch (Exception e) {
				errorHandler.accept(e);
			}
		});
	}

	@Override
	public void update(String key, Function<Optional<TYPE>, TYPE> valueHandler, Consumer<Throwable> errorHandler,
					   Runnable onSuccess, Consumer<Runnable> scheduler) {
		sync.submit(()->{
			try{
				TYPE result = valueHandler.apply(Optional.ofNullable(load(key)));

				save(key, duplicate.apply(result));

				if(onSuccess != null){
					scheduler.accept(onSuccess);
				}
			}catch (Exception e){
				errorHandler.accept(e);
			}
		});
	}

	@Override
	public void remove(String key, Consumer<Throwable> errorHandler) {
		sync.submit(()->{
			try {
				remove(key);
			} catch (IOException e) {
				errorHandler.accept(e);
			}
		});
	}

}
