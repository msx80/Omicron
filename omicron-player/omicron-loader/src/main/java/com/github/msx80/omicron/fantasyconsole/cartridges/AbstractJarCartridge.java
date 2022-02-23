package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.util.Properties;

import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.utils.FileUtil;

public abstract class AbstractJarCartridge implements Cartridge {

	
	protected Loader loader;
	protected Properties properties;

	public AbstractJarCartridge(Loader loader) throws Exception {
		super();
		this.loader = loader;

		byte[] prop = loader.loadFile("omicron.properties");
		if(prop== null) throw new RuntimeException("Zip/jar does not contains omicron.properties file.");
		this.properties = FileUtil.loadProps(prop);

	}

	@Override
	public byte[] loadFile(String filePath) {
		String path = properties.getProperty(PROP_PKG).replace('.', '/')+'/';
		String cf = path+filePath;
		
		return loader.loadFile(cf);
	}


	@Override
	protected void finalize() throws Throwable {
		System.out.println("Finalizing cartridge *****");
		FileUtil.close(loader);
		super.finalize();
	}

	@Override
	public void close() {
		FileUtil.close(loader);
	}

	@Override
	public Properties getOmicronProperties() {
		return properties;
	}
	
	protected byte[] loadCustomClass(String filePath) {
		return loader.loadFile(filePath);
		/*
		ZipEntry entry = jarFile.getJarEntry(filePath);
		if (entry == null) {
			if(filePath.startsWith("/"))
			{
				// retry without leading slash
				throw new RuntimeException("Are we sure this is needed?");
				// if yes uncomment: return loadFile(filePath.substring(1));
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
		}*/
	}
	
	
}