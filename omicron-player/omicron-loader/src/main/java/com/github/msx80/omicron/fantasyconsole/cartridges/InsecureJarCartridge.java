package com.github.msx80.omicron.fantasyconsole.cartridges;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.wiseloader.ArbitratorClassLoader;
import com.github.msx80.wiseloader.BytesLoader;

public class InsecureJarCartridge extends AbstractJarCartridge implements Cartridge {
	private Game game;

	public InsecureJarCartridge(Loader file) throws Exception {
		super(file);
	}

	@Override
	public synchronized Game getGameObject() {
		if (game == null) {
			game = loadGameObject();
		}
		return game;
	}

	private Game loadGameObject() 
	{
		try {
			String className = properties.getProperty(PROP_PKG) + "." + properties.getProperty(PROP_MAIN);
			ClassLoader c = getAllowAllClassLoader(this::loadCustomClass);
			Class<?> userClass = c.loadClass(className);
			return (Game) userClass.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException("Unable to instantiate game object", e);
		}
	}

	public static ClassLoader getAllowAllClassLoader(BytesLoader bl) {
		ArbitratorClassLoader c = new ArbitratorClassLoader( bl) {

			@Override
			protected boolean allowClass(String name) {
				return true;
			}
			
		};

		return c;
	}

}
