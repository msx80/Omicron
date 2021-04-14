package com.github.msx80.omicron.api.adv;

import java.util.function.Consumer;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;


/**
 * This is an advanced version of Sys that offer some methods for chaining Games, so
 * that a Game can run a second Game (and so on). Each parent can get a result from the child Game when it
 * terminates. It's mainly useful to implement mega Game like a proper fantasy console
 * Resources (sheet, sounds, musics, etc) are separated and read from Game class resources, so that the
 * appropriate Classloader will be used
 *
 */
public interface AdvancedSys extends Sys {

	public interface KeyboardListener 
	{
		   public boolean keyDown (int keycode);
		   public boolean keyUp (int keycode);
		   public boolean keyTyped (char character);
	}


	/**
	 * Starts and pass control to another cartridge.
	 * This method returns immediately and should be called as the last instruction of the loop() method.
	 * No more loop() will be called for the current Game until the child one terminates, 
	 * after that, the onResult callback is called with the result from child game, then the execution of parent
	 * resumes
	 * @param cartridge
	 * @param onResult
	 */
	void execute(Cartridge cartridge, Consumer<String> onResult, Consumer<Throwable> onException);
	
	/**
	 * Terminate a running game, returning a value to parent game, if any.
	 * If this is the root game, the application is terminated.
	 * This method returns immediately and should be called as the last instruction of the update() method.
	 * @param result
	 */
	void quit(String result);
	
	/**
	 * 
	 * @param listener
	 */
	void activateKeyboardInput(KeyboardListener listener);
}
