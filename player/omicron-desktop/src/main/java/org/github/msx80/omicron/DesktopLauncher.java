package org.github.msx80.omicron;



import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.SysConfig;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	public static void launch (Game game, boolean fullScreen) {
		SysConfig s = game.sysConfig();
		launch(game, fullScreen, s.width*3,  s.height*3);
	}

	public static void launch(Game game, boolean fullScreen, int physicalWidth, int physicalHeight) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = physicalWidth;
		config.height = physicalHeight;
		config.fullscreen = fullScreen;
		new LwjglApplication(OmicronEngineLibgdx.getApplicationForLibgdx(game, new DesktopHardwareInterface()), config);
	}
	
	
	/**
	 * Start any class in the classpath as game
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Game g = (Game) Class.forName(args[0]).newInstance();
		launch(g, false);
	}
}
