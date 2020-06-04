package org.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public class JarCartridge implements Cartridge {
	File file;
	JarFile jarFile;
	
	public JarCartridge(File file) throws IOException {
		this.file = file;
		this.jarFile = new JarFile(file);

	}

	@Override
	public Game getGameObject() throws Exception {
		return null;
		/*Properties p = this.getOmicronProperties();
		String className = p.getProperty("pkg")+"."+p.getProperty("main");
		Class<?> userClass = new DynamicClassLoader(bl).loadClass(className);
		return (Game) userClass.newInstance();*/
	}

	@Override
	public byte[] loadFile(String filePath) throws Exception {
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
