package com.github.msx80.runtime.loader;

import java.io.File;
import java.util.Properties;

import com.github.msx80.omicron.Game;

public class Cartridge {
	
	private File cartridgeFile;
	private Game game;
	private byte[] cover;
	private Properties properties;
	private boolean needmouse;
	
	public Cartridge(File cartridgeFile, Game game, Properties properties, byte[] cover) {
		super();
		this.cover = cover;
		this.cartridgeFile = cartridgeFile;
		this.game = game;
		this.properties = properties;
		needmouse = "true".equals(properties.getProperty("mouse"));
	}

	public File getCartridgeFile()
	{
		return this.cartridgeFile;
	}
	
	public byte[] getCover()
	{
		return this.cover;
	}
	
	public Game getGame()
	{
		return this.game;
	}
	
	public Properties getProperties()
	{
		return this.properties;
	}

	public boolean needMouse() {
		return needmouse;
	}
	
	
}
