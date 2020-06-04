package org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.github.msx80.omicron.fantasyconsole.crazyclassloader.SClassLoader;

public interface BytesLoader {
	
	byte[] loadFile(String filePath);
	void close();
	
	default byte[] loadClass(String className)
	{
		String filePath = SClassLoader.toFilePath(className);
		return loadFile(filePath);
	}
	
	
	static File findFile(String filePath, File classPath) {
		File file = new File(classPath, filePath);
		return file.exists() ? file : null;
	}


	public static BytesLoader fileLoader(File file) {
		if (!file.exists()) {
			throw new RuntimeException("File does not exists " + file);
		} else if (file.isDirectory()) {
			return new DirectoryBytesLoader(file);
		} else {
			try {
				final JarFile jarFile = new JarFile(file);

				return new JarBytesLoader(jarFile);
			} catch (IOException e) {
				throw new RuntimeException("Unable to open file, not a zip?", e);
			}
		}
	}
	
	
	
	
	
}
