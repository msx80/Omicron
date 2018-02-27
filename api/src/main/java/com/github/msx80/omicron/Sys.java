package com.github.msx80.omicron;

public interface Sys {
	public static final int SCREEN_WIDTH = 384;
	public static final int SCREEN_HEIGHT = 192;
	
	Image loadImage(String filename);
	Image[][] loadSpritesheet(String filename, int sizex, int sizey);
	Sound loadSound(String filename);
	void clearScreen(float red, float green, float blue);
	void color(float red, float green, float blue, float alpha);
	void draw(Image image, int x, int y);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height);
	void draw(Image image, int x, int y, boolean flipX, boolean flipY, int width, int height, int angle);
	void write(Image[][] font, int x, int y, String text);
	void offset(int x, int y);
	void playSound(Sound sound);
	int availableControllers();
	Controller controller(int index);
	Mouse mouse();
	void dispose(Image image);
	void dispose(Sound sound);
	void exit(Object returnValue);
}
