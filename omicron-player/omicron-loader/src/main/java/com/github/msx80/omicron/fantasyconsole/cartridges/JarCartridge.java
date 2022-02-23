package com.github.msx80.omicron.fantasyconsole.cartridges;

import java.io.File;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;

@Deprecated
public class JarCartridge extends AbstractJarCartridge implements Cartridge {

	public JarCartridge(File file) throws Exception {
		super(null);
		// TODO Auto-generated constructor stub
	}
/*
	private Game game;

	public JarCartridge(File file) throws Exception {
		super(file);
	}

	@Override
	public synchronized Game getGameObject() {
		if (game == null) {
			game = loadGameObject();
		}
		return game;
	}

	private Game loadGameObject() {
		try {
			String className = properties.getProperty(PROP_PKG) + "." + properties.getProperty(PROP_MAIN);
			Class<?> userClass = new DynamicClassLoader( this::loadCustomClass ).loadClass(className);
			return (Game) userClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate game object", e);
		}
	}
*/

	@Override
	public Game getGameObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
