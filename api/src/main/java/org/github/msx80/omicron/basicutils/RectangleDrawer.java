package org.github.msx80.omicron.basicutils;

import org.github.msx80.omicron.api.Sys;

public class RectangleDrawer 
{

	public static void outline(Sys sys, int x, int y, int w, int h, int sheet, int color)
	{
	
		sys.fill(sheet, x, y, w, 1, color);
		sys.fill(sheet, x, y, 1, h, color);
		sys.fill(sheet, x+w-1, y, 1, h, color);
		sys.fill(sheet, x, y+h-1, w, 1, color);
		
	}
}
