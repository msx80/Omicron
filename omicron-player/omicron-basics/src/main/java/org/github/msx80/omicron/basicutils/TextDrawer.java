package org.github.msx80.omicron.basicutils;

public interface TextDrawer {
	
	public static enum Align {LEFT, CENTER, RIGHT};

	void print(String text, int x, int y, Align align);

	void print(String text, int x, int y);

	int width(String text);
	
	int height();

}