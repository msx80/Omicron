package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.utils.FileUtil;
import com.github.msx80.wiseloader.*;

public class SecureJarCartridge implements Cartridge {
	File file;
	JarFile jarFile;
	private Properties properties;
	private Game game;
	
	public SecureJarCartridge(File file) throws Exception {
		this.file = file;
		this.jarFile = new JarFile(file);

		byte[] prop = loadFileInternal("omicron.properties");
		if(prop== null) throw new RuntimeException("Zip/jar does not contains omicron.properties file.");
		this.properties = FileUtil.loadProps(prop);

	}

	@Override
	public synchronized Game getGameObject()  {
		if(game == null)
		{
			game = loadGameObject(); 
		}
		return game;
	}

	private Game loadGameObject(){
		try
		{
		String className = properties.getProperty(PROP_PKG)+"."+properties.getProperty(PROP_MAIN);
		WhitelistClassLoader c = new WhitelistClassLoader(new BytesLoader() {
			
			@Override
			public byte[] loadFile(String name) throws Exception {
				return SecureJarCartridge.this.loadCustomClass(name);
			}
		}, WhitelistedJDKClasses.LIST);
		
		c.allowClasses(
				"java.util.Comparator",
				"java.util.stream.IntStream",
				"java.util.function.ToIntFunction",
				"java.util.concurrent.Callable",
				"java.io.EOFException",
				"java.util.Map$Entry",
				"com.github.msx80.omicron.api.Game",
				"com.github.msx80.omicron.api.Sys",
				"com.github.msx80.omicron.api.SysConfig",
				"com.github.msx80.omicron.api.SysConfig$VirtualScreenMode",
				"com.github.msx80.omicron.api.Pointer",
				"com.github.msx80.omicron.api.Controller",

				"java.lang.Boolean",
				"java.util.function.Predicate",
				"java.math.BigInteger",
				"java.util.Arrays",
				"java.util.function.IntConsumer",
				"java.util.function.Supplier",
				"java.util.regex.Pattern",
				"java.lang.Iterable",
				"java.lang.Class",
				"java.io.InputStream",
				"java.io.OutputStream",
				"java.io.ByteArrayOutputStream",
				"java.io.ByteArrayInputStream",
				"java.util.zip.GZIPOutputStream",
				"java.util.zip.GZIPInputStream",
				"java.lang.IllegalArgumentException");
		Class<?> userClass = c.loadClass(className);
		return (Game) userClass.newInstance();
		}
		catch(Throwable e)
		{
			throw new RuntimeException("Unable to instantiate game object", e);
		}
	}
	
	private byte[] loadCustomClass(String filePath) {
		ZipEntry entry = jarFile.getJarEntry(filePath);
		if (entry == null) {
			if(filePath.startsWith("/"))
			{
				// retry without leading slash
				return loadFile(filePath.substring(1));
			}
			else
			{
				return null;
			}
		}
		try {
			return FileUtil.readData(jarFile.getInputStream(entry));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] loadFile(String filePath) {
		String path = properties.getProperty(PROP_PKG).replace('.', '/')+'/';
		String cf = path+filePath;
		
		return loadFileInternal(cf);
	}

	public byte[] loadFileInternal(String cf) {
		ZipEntry entry = jarFile.getJarEntry(cf);
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
		System.out.println("Finalizing cartridge *****");
		FileUtil.close(jarFile);
		super.finalize();
	}

	@Override
	public void close() {
		FileUtil.close(jarFile);
		
	}

	@Override
	public Properties getOmicronProperties() {
		return properties;
	}
	
	
	
}
