package org.github.msx80.omicron.libretro.entrypoint;

import java.io.File;

import org.github.msx80.omicron.GdxOmicron;
import org.github.msx80.omicron.GdxOmicronOptions;
import org.github.msx80.omicron.HardwareInterface;
import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.fantasyconsole.GameLoadingUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxNativesLoader;

import experimental.LibretroApplication;
import experimental.LibretroGraphics;
import experimental.NullHardwareInterface;


/*
 * 

This is a static entry point for the Libretro core. All communication between the core and the java side are throu this static class.




ORDER OF CALLS (empirically derived)

[BACK] retro_init							   do nothing

[BACK] retro_load_game                         init or reinit JVM. Here we get filename of the jar (or buffer), read screen info
 --- not a callback [BACK] retro_init_hw_context
[BACK] retro_get_system_av_info				   here we should reply with width and height
[BACK] Context reset!                          here we can start opengl stuff

(gone fullscreen here)
[BACK] Context destroy!                        stop opengl eventually keeping "Game"
[BACK] Context reset!						   restart opengl with previous "Game"

(gone back to windowed here)
[BACK] Context destroy!
[BACK] Context reset!

(closed game here)
[BACK] Context destroy!						   stop opengl eventually keeping "Game"
[BACK] retro_unload_game					   throw away Game.

[BACK] retro_deinit                            



 */


public class EntryPoint{
	
	// controller and mouse button bits
	public static final int J_UP = (1 << 0);
	public static final int J_DOWN = (1 << 1);
	public static final int J_LEFT = (1 << 2);
	public static final int J_RIGHT = (1 << 3);
	public static final int J_A = (1 << 4);
	public static final int J_B = (1 << 5);
	public static final int J_X = (1 << 6);
	public static final int J_Y = (1 << 7);
	public static final int M_0 = (1 << 8);
	public static final int M_1 = (1 << 9);
	public static final int M_2 = (1 << 10);

	

	static SysConfig s = null; // the system configuration required by the actual omicron cartridge 
	
	static GdxOmicron engine;
	
	static {
		Configuration.DEBUG.set(true);
	}
	
	static String gameToLoad = null;
	static Game game = null;
	
	public static void callLoop(int ctrlStat, int mx, int my) {
		// this is the main loop function. The parameters are the controllers and mouse status
		try
		{
			if(s==null) return; // this indicate no actual game was loaded.
			
			// update FPS counters on Graphics
			((LibretroGraphics) Gdx.graphics).update();
			
			// prepare input received, inject them into the system
			parseInput(ctrlStat, mx, my);
			
			Gdx.gl.glViewport(0, 0, s.width, s.height); // to do every loop as per GL Core docs
	
			// call the render on Omicron engine, which will call update and render on the Game
			engine.render();
			
			// here we should unbind everything, TODO check if something is left bound.
			Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
			
		} catch (Exception e) {e.printStackTrace();throw e; }
	}

	private static void parseInput(int ctrlStat, int mx, int my) {
		
		// TODO the following is all shortcuts working only for Omicron
		// to be working more generically with libgdx, a gdx Input class should be defined and attached to Gdx.input
		
		Controller c = engine.controllers()[0];
		c.down = (ctrlStat & J_DOWN) != 0;
		c.up = (ctrlStat & J_UP) != 0;
		c.left = (ctrlStat & J_LEFT) != 0;
		c.right = (ctrlStat & J_RIGHT) != 0;

		c.btn[0] = (ctrlStat & J_A) != 0;
		c.btn[1] = (ctrlStat & J_B) != 0;
		c.btn[2] = (ctrlStat & J_X) != 0;
		c.btn[3] = (ctrlStat & J_Y) != 0;
		
		Mouse m = engine.mouse();
		m.x = mx;
		m.y = my;
		m.btn[0] = (ctrlStat & M_0) != 0;
		m.btn[1] = (ctrlStat & M_1) != 0;
		m.btn[2] = (ctrlStat & M_2) != 0;
		
	}

	public static void callSetupContext()
	{
		try
		{
			// set up the Opengl context.
			// See: http://forum.lwjgl.org/index.php?topic=6992.0
			System.out.println("Loading gdx natives");
			GdxNativesLoader.load();
			System.out.println("Calling createCapabilities");
			GL.createCapabilities();
			System.out.println("Called createCapabilities, all good");
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw e; 
		}
	}
	public static void callLoadGame(String filename)
	{
		// simply store the cartridge file to load
		gameToLoad = filename;
		System.out.println("Loading game: "+filename);
		game = GameLoadingUtils.loadGameFromJar(new File(gameToLoad));
		// obtain desired system configuration from cartridge.
		s = game.sysConfig();

	}
	public static void callSetup() {
		try
		{
			System.out.println("Calling JAVA setup");

			// TODO this is actually called on context creation right after callSetupContext.
			// if coming from fullscreen toggle or such, we don't have the game anymore
			//if(game==null) game = GameLoadingUtils.loadGameFromJar(new File(gameToLoad));
			
			// actually load the game from the cartridge.
			// this parse the included omicron.properties descriptor and use a custom classloader to load classes in a sandbox.
			 
			System.out.println("Game loaded, initializing engine");
			engine = new GdxOmicron(game, new NullHardwareInterface(), new GdxOmicronOptions().setRenderingToTexture(true));
			
			new LibretroApplication(engine); // stores itself as Gdx.app
			
	
		} catch (Exception e) {e.printStackTrace();throw e; }
	}

	public static int sysInfo(int iswidth)
	{
		System.out.println("Desired system info: "+s);
		if(iswidth!=0) return s.width; else return s.height;
	}
	
	public static void callTeardown() {
		
		// this is called on OPENGL context destruction, should at least finalize the engine object.
		// TODO do some cleaning ?

	}

	
}