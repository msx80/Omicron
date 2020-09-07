package org.github.msx80.omicron;

import java.math.BigInteger;

// This is a copy of Colors from omicron-basics. It's pretty simple so i'm copying it here to avoid 
// having the engine depend on it
public class ColorsCopy 
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
	
	public static String str(int c)
	{
		return Long.toString(c, 16); // format in hex
	}
	public static int fromHex(String string) {
		BigInteger b = new BigInteger(string, 16);
		return (b.intValue() << 8) | 255;
	}
	
	public static int alpha(int c)
	{
		return c & 0xFF;
	}
}
