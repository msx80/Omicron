package org.github.msx80.omicron.basicutils;

public class Colors 
{
	public static final int BLACK = from(0,0,0);
	public static final int WHITE = from(255,255,255);
	public static final int RED = from(255,0,0);
	public static final int GREEN = from(0,255,0);
	public static final int BLUE = from(0,0,255);
	
	public static int from(int r, int g, int b, int a)
	{
		int color =  (r << 24) | (g << 16) | (b << 8) | a;
		return color;
	}
	public static int from(int r, int g, int b)
	{
		return from(r, g, b, 255);
	}
}