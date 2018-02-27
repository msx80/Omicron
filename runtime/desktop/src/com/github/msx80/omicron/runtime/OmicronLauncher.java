package com.github.msx80.omicron.runtime;

import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.msx80.omicron.Game;
import com.github.msx80.runtime.OmicronException;
import com.github.msx80.runtime.implementation.SysImpl;
import com.github.msx80.runtime.loader.Cartridge;

public class OmicronLauncher {
	public static void main (String[] arg) {
		
		//config.foregroundFPS = 30;
		//config.backgroundFPS = 30;
		
    if(arg.length!=1)
		{
			System.err.println("Please launch with a single parameter, the fully qualified name of the class to launch.");
			System.exit(2);
		}

		
		String cls = arg[0];
		
		System.out.println("Launching "+cls);
		Game g;
		try {
			g = (Game) Class.forName(cls).newInstance();
		} catch (Exception e) {
		
			e.printStackTrace();
			System.exit(42);
			return;
		}
		
		
		runGame(g);
	}

	public static void runGame(Game game) {
		Properties p = new Properties();
		try {
			p.load(game.getClass().getResourceAsStream("/omicron.properties"));
		} catch (IOException e) {
			throw new OmicronException(e);
		}
		Cartridge c = new Cartridge(null, game, p, null);
		
		String arch = System.getProperty("os.arch");
		System.out.println("Architecture: "+arch);
		if(arch.toLowerCase().startsWith("arm"))
		{
			System.out.println("TODO ARM");
		}
		else
		{
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			
			config.title = c.getProperties().getProperty("name")+" by "+c.getProperties().getProperty("author")+" ("+c.getProperties().getProperty("year")+") - Omicron";
			config.width=1366 ; //320*2;
			config.height=768;//192*2;
			config.fullscreen=true;
			
			//config.width=640;config.height=480;
			//config.width=384 ; config.height=192;	config.fullscreen=false;
			
			
			new LwjglApplication(new SysImpl(c), config);
		}
	}
}
