package org.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.github.msx80.omicron.api.Game;

public interface Cartridge {

	public Game getGameObject() throws Exception;
	public byte[] loadFile(String name) throws Exception;
	
	default Properties getOmicronProperties() throws Exception
	{
		byte[] prop = loadFile("omicron.properties");
		if(prop== null) throw new RuntimeException("Zip/jar does not contains omicron.properties file.");
		Properties p = new Properties();
		p.load(new ByteArrayInputStream(prop));
		return p;
	}
	public void close();
}
