package org.github.msx80.omicron;



import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.adv.Cartridge;

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
		//Game g = (Game) Class.forName(args[0]).newInstance();
		//launch(g, false);
	}
}
