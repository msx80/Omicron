package org.github.msx80.crazyclassloader;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.github.msx80.crazyclassloader.util.FileUtil;

public interface BytesLoader {
	
	byte[] loadFile(String filePath);
	
	default byte[] loadClass(String className)
	{
		String filePath = AggressiveClassLoader.toFilePath(className);
		return loadFile(filePath);
	}
	
	
	static File findFile(String filePath, File classPath) {
		File file = new File(classPath, filePath);
		return file.exists() ? file : null;
	}


	public static BytesLoader fileLoader(File file) {
		if (!file.exists()) {
			throw new RuntimeException("Path not exists " + file);
		} else if (file.isDirectory()) {
			return directoryLoader(file);
		} else {
			try {
				final JarFile jarFile = new JarFile(file);

				return jarLoader(jarFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static BytesLoader jarLoader(final JarFile jarFile) {
		return new BytesLoader() {
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
				FileUtil.close(jarFile);
				super.finalize();
			}
		};
	}
	
	
	public static BytesLoader directoryLoader(final File dir) {
		return filePath -> {
			File file = findFile(filePath, dir);
			if (file == null) {
				return null;
			}

			return FileUtil.readFileToBytes(file);
		};
	}
	
}
