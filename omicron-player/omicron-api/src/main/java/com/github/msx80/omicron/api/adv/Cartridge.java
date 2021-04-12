package com.github.msx80.omicron.api.adv;
import java.util.Properties;

import com.github.msx80.omicron.api.Game;

/**
 * Represent a game, tipically on a .omicron file.
 * See CartridgeLoadingUtils to load one.
 *
 */
public interface Cartridge {

	public static final String PROP_MAIN = "main";
	public static final String PROP_PKG = "pkg";
	public  static final String PROP_NAME = "name";

	
	/**
	 * Obtain the Game instance for this cartridge.
	 * The same object is returned each time, to get a new one
	 * load the cartridge again.
	 * @return
	 */
	public Game getGameObject();
	
	/**
	 * Load a resource from the cartridge (tipically gfx, music, etc)
	 * @param name
	 * @return
	 */
	public byte[] loadFile(String name);
	
	/**
	 * Get the properties from the omicron.property file in the cartridge.
	 */
	public Properties getOmicronProperties();
	
	/**
	 * Close the cartridge releasing all resources.
	 */
	public void close();
}
