package com.github.msx80.omicron.libretro.entrypoint;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.linux.DynamicLinkLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.github.msx80.omicron.ControllerImpl;
import com.github.msx80.omicron.GdxOmicron;
import com.github.msx80.omicron.GdxOmicronOptions;
import com.github.msx80.omicron.MouseImpl;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.adv.Cartridge;
import com.github.msx80.omicron.fantasyconsole.CartridgeLoadingUtils;


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

	
	static boolean libraryLoaded = false;
	
	static SysConfig s = null; // the system configuration required by the actual omicron cartridge 
	
	static GdxOmicron engine;
	
	
	
	static String gameToLoad = null;
	static Cartridge cartridge = null;
	
	public static void callLoop(int ctrlStat, int mx, int my) {
		// this is the main loop function. The parameters are the controllers and mouse status
		
		//logEntry("Inside loop!");
		try
		{
			if(s==null) return; // this indicate no actual game was loaded.
			
			//logEntry("Calling update..");
				
			// update FPS counters on Graphics
			((LibretroGraphics) Gdx.graphics).update();
			
			//logEntry("Parsing input..");
			// prepare input received, inject them into the system
			parseInput(ctrlStat, mx, my);
			
			//logEntry("Setting viewport..");
			Gdx.gl.glViewport(0, 0, s.width, s.height); // to do every loop as per GL Core docs
	
			//logEntry("Rendering..");
			// call the render on Omicron engine, which will call update and render on the Game
			try {
				engine.render();
			} catch (Exception e) {
				logEntry("EXCEPTION IN RENDER");
				e.printStackTrace();
			}
			
			//logEntry("Binding texture 0..");
			// here we should unbind everything, TODO check if something is left bound.
			Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
			
		} catch (Exception e) {e.printStackTrace();throw e; }
	}

	private static void parseInput(int ctrlStat, int mx, int my) {
		
		// TODO the following is all shortcuts working only for Omicron
		// to be working more generically with libgdx, a gdx Input class should be defined and attached to Gdx.input
		
		ControllerImpl c = (ControllerImpl) engine.controllers()[0];
		c.down = (ctrlStat & J_DOWN) != 0;
		c.up = (ctrlStat & J_UP) != 0;
		c.left = (ctrlStat & J_LEFT) != 0;
		c.right = (ctrlStat & J_RIGHT) != 0;

		c.btn[0] = (ctrlStat & J_A) != 0;
		c.btn[1] = (ctrlStat & J_B) != 0;
		c.btn[2] = (ctrlStat & J_X) != 0;
		c.btn[3] = (ctrlStat & J_Y) != 0;
		
		MouseImpl m = (MouseImpl) engine.pointers()[0];
		m.x = mx;
		m.y = my;
		m.btn[0] = (ctrlStat & M_0) != 0;
		m.btn[1] = (ctrlStat & M_1) != 0;
		m.btn[2] = (ctrlStat & M_2) != 0;
		
	}

	public static void callResetContext()
	{
		logEntry("CONTEXT RESET ****");
		try
		{
			// set up the Opengl context.
			// See: http://forum.lwjgl.org/index.php?topic=6992.0
			logEntry("Calling createCapabilities");
			GL.createCapabilities();
			logEntry("Called createCapabilities, all good");

			logEntry("Initializing engine");

			if(engine == null)
			{
				logEntry("New engine, cartridge is: "+cartridge);
				engine = new GdxOmicron(cartridge, new NullHardwareInterface(), new GdxOmicronOptions().setRenderingToTexture(true));
				logEntry("Engine loaded: "+engine);
				
				Object o = new LibretroApplication(engine); // stores itself as Gdx.app
				logEntry("LibretroApplication started: "+o);
			}
			else
			{
				logEntry("Engine already initialized, calling context reset");
				engine.contextReset();
			}
			
			logEntry("Java Setup OK!");

			
			
			
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e); 
		}
	}

	private static void loadLibraries() {
		deepbindTrick();
		

		// technically in linux we can skip this since it's already loaded in the trick above
		//if(!SharedLibraryLoader.isLinux)
		//{
			logEntry("Loading gdx natives");
			GdxNativesLoader.load();
		//}
	}

	private static void deepbindTrick() {
		if(SharedLibraryLoader.isLinux)
		{

/*

So i encountered a nasty bug while working on linux. Upon starting a game, the whole system would crash
with the following error:

retroarch: allocator.c:177: sixel_allocator_realloc: Assertion `allocator' failed.
./test.sh: line 6:  1993 Aborted                 (core dumped) /usr/bin/retroarch -L etc..

I first tought it was some mismatch with opengl, but after putting LOTS of debug "printf" around, i found 
that it crashed when invoking Gdx2DPixmap.load, a native method.
I searched for that "sixel" to find that it was an obscure image format loader. But libgdx wasn't using it.
But retroarch was. I tought that maybe some library mismatch was sending the program to the wrong call or something.
I fired up gdb and dusted off my knowledge of it and sure enought something was fishy:

Thread 1 "retroarch" received signal SIGABRT, Aborted.
__GI_raise (sig=sig@entry=6) at ../sysdeps/unix/sysv/linux/raise.c:51
51      ../sysdeps/unix/sysv/linux/raise.c: No such file or directory.
(gdb) list
46      in ../sysdeps/unix/sysv/linux/raise.c
(gdb) trace
Tracepoint 1 at 0x7fffedadcfb7: file ../sysdeps/unix/sysv/linux/raise.c, line 51.
(gdb) bt
#0  __GI_raise (sig=sig@entry=6) at ../sysdeps/unix/sysv/linux/raise.c:51
#1  0x00007fffedade921 in __GI_abort () at abort.c:79
#2  0x00007fffedace48a in __assert_fail_base (fmt=0x7fffedc55750 "%s%s%s:%u: %s%sAssertion `%s' failed.\n%n", assertion=assertion@entry=0x7ffff1f5740$
   file=file@entry=0x7ffff1f573f9 "allocator.c", line=line@entry=177, function=function@entry=0x7ffff1f574a0 "sixel_allocator_realloc") at assert.c:$
#3  0x00007fffedace502 in __GI___assert_fail (assertion=0x7ffff1f57405 "allocator", file=0x7ffff1f573f9 "allocator.c", line=177, function=0x7ffff1f57$
#4  0x00007ffff1f51470 in sixel_allocator_realloc () from /usr/lib/x86_64-linux-gnu/libsixel.so.1
#5  0x00007ffff1f44363 in ?? () from /usr/lib/x86_64-linux-gnu/libsixel.so.1
#6  0x00007ffff1f45de9 in ?? () from /usr/lib/x86_64-linux-gnu/libsixel.so.1
#7  0x00007ffff1f48b9f in ?? () from /usr/lib/x86_64-linux-gnu/libsixel.so.1
#8  0x00007ffff1f48cf8 in stbi_load_from_memory () from /usr/lib/x86_64-linux-gnu/libsixel.so.1
#9  0x00007fff7fdf2bbc in gdx2d_load () from /tmp/libgdxnicola/8551b6a6/libgdx64.so
#10 0x00007fff7fde03fc in Java_com_badlogic_gdx_graphics_g2d_Gdx2DPixmap_load () from /tmp/libgdxnicola/8551b6a6/libgdx64.so
#11 0x00007fffc0e4e345 in ?? ()
#12 0x00007fffffffa398 in ?? ()
#13 0x00007fffc0e4e0cd in ?? ()
#14 0x0000000000000000 in ?? ()

In line #9 it's calling gdx2d_load from libgdx64.so, the next line (#8) is calling stbi_load_from_memory, 
but from libsixel.so! Turns out that gdx includes a library called stb image (https://github.com/nothings/stb/blob/master/stb_image.h)
which define stbi_load_from_memory, but the same library was used by libsixel! So we have a conflict: two different
.so library have the same symbol (stbi_load_from_memory). Since libsixel is loaded when retroarch starts, by the time libgdx64 is loaded the symbol
is already taken. For how linux works, the symbol is resolved to the one in libsixel, crashing the app.

To verify the issue, i started retroarch preloading libgdx64 with LD_PRELOAD:
LD_PRELOAD=/path/to/libgdx64.so retroarch 

This runs the program with the given libraries already loaded, so they would take precedence. And behold, the
core was now starting correctly and running fine!

Problem is: i can't expect all people to use LD_PRELOAD to run the core. I spent some hours googling
about linux internal working and finally landed on this StackOverflow question:

https://stackoverflow.com/questions/22004131/is-there-symbol-conflict-when-loading-two-shared-libraries-with-a-same-symbol

So i was right:

"When you load a library with dlopen you can access all symbols in it with dlsym and those symbols will be
the correct symbols from that library and doesn't pollute the global symbol space (unless you used RTLD_GLOBAL).
But its dependencies are still resolved using the already loaded global symbols if available even if the
library itself defines the symbol."

It also suggests a solution: there's a flag in dlopen() that solves just this problem: bind all symbold in
a library to the library itself and not from the global symbol table.

Problem is, libgdx ultimately loads libraries with System.load, which doesn't let you specify any flag.
Luckly, i found out that LWJGL is using a different system, implemented in their own native library, 
which lets you call dlopen() directly and with flags. 

So the following code estract the libgdx64.so from the jars (as gdk normally does) and then loads it
with LWJGL facility, using the RTLD_DEEPBIND flag.
*/



			logEntry("Dynamically loading library with DEEPBIND");
			SharedLibraryLoader sl = new SharedLibraryLoader();
			String platformName = sl.mapLibraryName("gdx");
			logEntry("Platform name: "+platformName);
			try {
				File f = sl.extractFile(platformName, null);
				System.out.println(f.getCanonicalPath());
				DynamicLinkLoader.dlopen(f.getCanonicalPath(), DynamicLinkLoader.RTLD_DEEPBIND | DynamicLinkLoader.RTLD_NOW);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}
	private static void logEntry(String string) {
		System.out.println("ENTRYPOINT: "+string);
		
	}

	public static void callLoadGame(String filename)
	{
		if(!libraryLoaded)
		{
			loadLibraries();
			libraryLoaded = true;
		}
		
		// simply store the cartridge file to load
		logEntry("Loading game: "+filename+", Engine is: "+engine);
		gameToLoad = filename;
		cartridge = CartridgeLoadingUtils.fromOmicronFile(new File(gameToLoad));
		s = cartridge.getGameObject().sysConfig();
		logEntry("Game loaded, sysconfig: "+s);
	}
	
	public static void callUnloadGame()
	{
		logEntry("UNLOADGAME ****");
		// context is already destroyed.
		engine = null;
		gameToLoad = null;
		cartridge = null;
		s = null;
		logEntry("UNLOADGAME DONE ****");
	}

	public static int sysInfo(int iswidth)
	{
		logEntry("SYSINFO ****");
		logEntry("Desired system info: "+s);
		if(iswidth!=0) return s.width; else return s.height;
	}
	
	public static void callContextDestroy() {
		
		// this is called on OPENGL context destruction, should at least finalize the engine object.
		// TODO do some cleaning ?
		logEntry("CONTEXTDESTROY ****");
		if(engine!=null) engine.contextDestroy();
		
	}


}
