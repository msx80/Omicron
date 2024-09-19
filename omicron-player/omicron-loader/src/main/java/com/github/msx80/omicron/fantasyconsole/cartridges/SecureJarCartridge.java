package com.github.msx80.omicron.fantasyconsole.cartridges;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.wiseloader.BytesLoader;
import com.github.msx80.wiseloader.WhitelistClassLoader;
import com.github.msx80.wiseloader.WhitelistedJDKClasses;

public class SecureJarCartridge extends AbstractJarCartridge implements Cartridge {
	private Game game;

	public SecureJarCartridge(Loader file) throws Exception {
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
			ClassLoader c = getWhitelistClassLoader(this::loadCustomClass);
			Class<?> userClass = c.loadClass(className);
			return (Game) userClass.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException("Unable to instantiate game object", e);
		}
	}

	public static ClassLoader getWhitelistClassLoader(BytesLoader bl) {
		WhitelistClassLoader c = new WhitelistClassLoader( bl, false, WhitelistedJDKClasses.LIST);

		c.allowClasses(
				"java.lang.Class",
				"com.github.msx80.omicron.api.Game", 
				"com.github.msx80.omicron.api.Sys",
				"com.github.msx80.omicron.api.SysConfig",
				"com.github.msx80.omicron.api.SysConfig$VirtualScreenMode", 
				"com.github.msx80.omicron.api.Pointer",
				"com.github.msx80.omicron.api.Controller",
				"com.github.msx80.omicron.basicutils.MapDrawer$MapData",
				"com.github.msx80.omicron.basicutils.MapDrawer$MapDataArray");
		return c;
	}

}
