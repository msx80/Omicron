package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class JarLoader implements Loader {

	protected File file;
	protected JarFile jarFile;
	protected Properties properties;

	public JarLoader(File file) throws Exception {
		super();
		this.file = file;
		this.jarFile = new JarFile(file);
	}

	@Override
	public byte[] loadFile(String filePath) {
		ZipEntry entry = jarFile.getJarEntry(filePath);
		if (entry == null) {
			
				return null;
			
		}
		try {
			return FileUtil.readData(jarFile.getInputStream(entry));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	protected void finalize() throws Throwable {
		System.out.println("Finalizing JarLoader *****");
		FileUtil.close(jarFile);
		super.finalize();
	}

	@Override
	public void close() {
		FileUtil.close(jarFile);
	}

	@Override
	public void forEachFile(Consumer<String> consumer) {
		Enumeration<JarEntry> en = jarFile.entries();
		while (en.hasMoreElements()) {
			JarEntry je = en.nextElement();
			consumer.accept(je.getName());
		}
		
	}
	
}