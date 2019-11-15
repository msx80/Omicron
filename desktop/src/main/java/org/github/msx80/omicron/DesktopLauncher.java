package org.github.msx80.omicron;



import java.awt.Desktop;
import java.net.URI;

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
		new LwjglApplication(new GdxOmicron(game, new HardwareInterface() {
			
			public void openUrl(String url) throws Exception {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				    Desktop.getDesktop().browse(new URI(url));
				}
				else
				{
					throw new UnsupportedOperationException("Operation not supported");
				}
				
			}
		}), config);
	}
	
	
}
