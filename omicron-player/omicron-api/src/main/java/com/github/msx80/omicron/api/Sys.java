package com.github.msx80.omicron.api;

public interface Sys 
{
	/**
	 * Draws a portion of an image into the screen. The current color is applied to the image.
	 * @param surfNum the surface from which to copy the image. References the file "surface<num>.png" from Resources folder.
	 * @param x
	 * @param y
	 * @param srcx
	 * @param srcy
	 * @param w
	 * @param h
	 * @param rotate
	 * @param flip
	 */
    void draw(int surfaceNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip);
    /**
     * Create a new surface with the specified dimension. Result can be used as surfNum in all context.
     * @param w
     * @param h
     * @return
     */
    int newSurface(int w, int h);
    /**
     * Return the color of a pixel in a specific surface.
     * @param surface 0 for screen
     * @param x
     * @param y
     * @return
     */
    int getPix(int surfaceNum, int x, int y);
    
    /**
     * Fill an area of a surface with an uniform color.
     * @param surfaceNum 0 for screen, or any surface number
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     */
    void fill(int surfaceNum, int x, int y, int w, int h, int color);
    
    /**
     * move the origin (0,0) by the specified offset.
     * Note: offset is reset before every loop();
     * @param x
     * @param y
     */
	void offset(int x, int y);
	
	/**
	 * Clear the screen with a specific color
	 * Note: alpha is ignored, it's always full alpha
	 * @param color
	 */
	void clear(int color);
	
	/**
	 * Set a color for subsequent draw() calls. The color will be multiplied to the pixels being written.
	 * Set to white full alpha for direct copy.
	 * @param color
	 */
	void color(int color);
	
	/**
	 * Return current frame per second.
	 * @return
	 */
	int fps();
	
	/**
	 * Get a value from persistent memory.
	 * @param key
	 * @return
	 */
	String mem(String key);
	
	/**
	 * Write a value to persistent memory. It will be available on the next run of the game.
	 * Note: all data are associated to the "code" passed on SysConfig
	 * @param key
	 * @param value
	 */
	void mem(String key, String value);
	
	/**
	 * Return the state of the pointers (ie, mouse or touch devices).
	 * At least one pointer is granted to be returned.
	 * @return
	 */
	Pointer[] pointers();
	
	/**
	 * Return the state of all available controllers.
	 * Firse one is the keyboard, then all gamepads/joysticks.
	 * @return
	 */
	Controller[] controllers();
	
	/**
	 * Play a sound
	 * @param soundNum
	 * @param volume
	 * @param pitch
	 */
	void sound(int soundNum, float volume, float pitch);
	
	/**
	 * Load a binary file into a byte array. Files are called file<num>.bin
	 * @param fileNum
	 * @return
	 */
	byte[] binfile(int fileNum);
		
	/**
	 * Play a music. Music files are called music<num>.mp3
	 * @param musicNum
	 * @param volume
	 * @param loop
	 */
	void music(int musicNum, float volume, boolean loop);
	
	/**
	 * Stop currently playing music 
	 */
	void stopMusic();
	
	/**
	 * Execute some customized hardware related command
	 * @param module
	 * @param command
	 * @param param
	 * @return
	 */
	Object hardware(String module, String command, Object param);
	
	/**
	 * Set the current clip of the screen, that is the area that will be drawn onto.
	 * Anything drawn outside of the clip area will not be drawn.
	 * Set all parameters to 0 to reset the clipping area
	 * @param x the rectangle area to clip
	 * @param y the rectangle area to clip
	 * @param w the rectangle area to clip
	 * @param h the rectangle area to clip
	 */
	void clip(int x, int y, int w, int h);
	
	long millis();
	void trace(String s);
}
