package org.github.msx80.omicron.fantasyconsole.crazyclassloader.bytesloader;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class JarBytesLoader implements BytesLoader {

	private JarFile jarFile;
	
	public JarBytesLoader(JarFile jarFile) {
		this.jarFile = jarFile;
	}

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

	@Override
	public void close() {
		FileUtil.close(jarFile);
		
	}

}
