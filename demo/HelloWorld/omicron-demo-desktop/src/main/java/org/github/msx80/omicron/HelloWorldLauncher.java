package org.github.msx80.omicron;



import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.SysConfig;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class HelloWorldLauncher {
	public static void launch (Game game) {
		SysConfig s = game.sysConfig();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = s.width*3;
		config.height = s.height*3;
		new LwjglApplication(new GdxOmicron(game), config);
	}
	
	public static void main(String[] args)
	{
		launch(new HelloWorld());
	}
}
