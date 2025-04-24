package com.github.msx80.omicron;



import java.io.InputStream;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.cartridges.ClasspathCartridge;

public class DesktopLauncher {
	
	public static void launch (Cartridge cartridge, boolean fullScreen, String[] args) {
		SysConfig s = cartridge.getGameObject().sysConfig();
		launch(cartridge, fullScreen, args, s.width*3,  s.height*3);
	}

	public static void launch(Cartridge cartridge, boolean fullScreen, String[] args, int physicalWidth, int physicalHeight) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(physicalWidth, physicalHeight);
		//config.width = physicalWidth;
		//config.height = physicalHeight;
		//config.set
		//config.fullscreen = fullScreen;
		//config.forceExit = false;
		config.setWindowPosition(50, 50);
		config.setForegroundFPS(60);
		config.setIdleFPS(0);
		
		// if we are running a classpath cartridge, we could have an icon available, let's use it for the window.
		try(InputStream is = cartridge.getClass().getResourceAsStream("/omicronIcon.png"))
		{
			if(is != null)
			{
				config.setWindowIcon(FileType.Classpath, "omicronIcon.png");
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		new Lwjgl3Application(OmicronEngineLibgdx.getApplicationForLibgdx(cartridge, new DesktopHardwareInterface(args)), config);

		/*
		 * for lwjgl 2
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = physicalWidth;
		config.height = physicalHeight;
		config.fullscreen = fullScreen;
		config.forceExit = false;
		new LwjglApplication(OmicronEngineLibgdx.getApplicationForLibgdx(cartridge, new DesktopHardwareInterface(args)), config);
		*/
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
