package org.github.msx80.omicron.api;

public interface Sys 
{
	/**
	 * Draws a portion of an image into the screen. The current color is applied to the image.
	 * @param sheetNum the sheet from which to copy the image. References the file "sheet<num>.png" from Resources folder.
	 * @param x
	 * @param y
	 * @param srcx
	 * @param srcy
	 * @param w
	 * @param h
	 * @param rotate
	 * @param flip
	 */
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate, int flip);
    int newSurface(int w, int h);
    int getPix(int sheetNum, int x, int y);
    void fill(int sheetNum, int x, int y, int w, int h, int color);
	void offset(int x, int y);
	/**
	 * Note: alpha is ignored, it's always full alpha
	 * @param color
	 */
	void clear(int color);
	void color(int color);
	int fps();
	String mem(String key);
	void mem(String key, String value);
	Mouse mouse();
	Controller[] controllers();
	void sound(int soundNum, float volume, float pitch);
	void music(int musicNum, float volume, boolean loop);
	void stopMusic();
	String hardware(String module, String command, String param);
}
