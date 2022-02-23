package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class FolderLoader implements Loader {

	protected Path folder;
	protected Properties properties;

	public FolderLoader(Path folder) throws Exception {
		super();
		this.folder = folder;
	

	}

	@Override
	public byte[] loadFile(String filePath) {
		Path p = folder.resolve(filePath);

		if (!Files.exists(p)) {
			return null;
		}
		try {
			return Files.readAllBytes(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public void close() {

	}

	@Override
	public void forEachFile(Consumer<String> consumer) {
		
		try {
			scan(folder, consumer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	private void scan(Path parent, Consumer<String> consumer) throws IOException {
		Files.list(parent).forEach(p -> {
			consumer.accept(folder.relativize(p).toString().replace('\\', '/'));
			if(Files.isDirectory(p))
			{
				try {
					scan(p, consumer);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	
	
}