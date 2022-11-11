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

package space.quinoaa.spigotcommons.test.data;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import space.quinoaa.spigotcommons.data.keyvalue.Serializer;
import space.quinoaa.spigotcommons.data.keyvalue.UnsafeFSStore;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
public class UnsafeFSDataTest {
	static final Path path = Paths.get("test");

	boolean finished = false;
	@SuppressWarnings("all") Optional<DataType> ret = Optional.empty();
	ExecutorService exec = Executors.newSingleThreadExecutor();

	@SuppressWarnings("all")
	@Test
	public void testData() throws IOException, InterruptedException {
		Serializer<DataType> ser = new Serializer<>(DataType::serialize, DataType::deserialize, DataType::duplicate);
		UnsafeFSStore<DataType> s = new UnsafeFSStore<>(path, ser, 0);

		s.update("aaaa", opt->new DataType("1234"), Throwable::printStackTrace);

		load(s, "aaaa");
		Assertions.assertEquals("1234", ret.get().data);

		s.update("aaaa", opt-> {
			DataType data = opt.get();
			data.data = data.data + "5678";
			return data;
		}, Throwable::printStackTrace);

		load(s, "aaaa");
		Assertions.assertEquals("12345678", ret.get().data);


		load(s, "abcd");
		Assertions.assertFalse(ret.isPresent());

		s.remove("aaaa", Throwable::printStackTrace);
		load(s, "aaaa");
		Assertions.assertFalse(ret.isPresent());


	}

	private void load(UnsafeFSStore<DataType> s, String key) throws InterruptedException {
		finished = false;
		s.loadCopy(key, data->{
			finished = true;
			ret = data;
		}, error-> finished = true, exec::submit);
		waitTilFinish();
	}


	@SuppressWarnings("all")
	private void waitTilFinish() throws InterruptedException {
		while(!finished){
			Thread.sleep(10L);
		}
	}

}
