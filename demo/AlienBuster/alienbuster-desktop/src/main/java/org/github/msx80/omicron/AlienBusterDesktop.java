package org.github.msx80.omicron;



import org.github.msx80.omicron.alienbuster.AlienBusterGame;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.ScreenConfig;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class AlienBusterDesktop {
	public static void launch (Game game) {
		ScreenConfig s = game.screenConfig();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = s.width*2;
		config.height = s.height*2;
		new LwjglApplication(new GdxOmicron(game), config);
	}
	
	public static void main(String[] args)
	{
		launch(new AlienBusterGame());
	}
}
