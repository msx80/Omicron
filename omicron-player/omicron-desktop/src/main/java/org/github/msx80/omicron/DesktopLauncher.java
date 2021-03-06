package org.github.msx80.omicron;



import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.adv.Cartridge;
import org.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;

import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	public static void launch (Cartridge cartridge, boolean fullScreen, String[] args) {
		SysConfig s = cartridge.getGameObject().sysConfig();
		launch(cartridge, fullScreen, args, s.width*3,  s.height*3);
	}

	public static void launch(Cartridge cartridge, boolean fullScreen, String[] args, int physicalWidth, int physicalHeight) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = physicalWidth;
		config.height = physicalHeight;
		config.fullscreen = fullScreen;
		new LwjglApplication(OmicronEngineLibgdx.getApplicationForLibgdx(cartridge, new DesktopHardwareInterface(args)), config);
	}
	
	
	/**
	 * Start any class in the classpath as game
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		int n = args[0].lastIndexOf('.');
		String pkg = args[0].substring(0,  n);
		String main = args[0].substring(n+1);
		
		ClasspathCartridge c = new ClasspathCartridge(args[0], pkg, main);
		launch(c, false, args);
		//Game g = (Game) Class.forName(args[0]).newInstance();
		//launch(g, false);
	}
}
