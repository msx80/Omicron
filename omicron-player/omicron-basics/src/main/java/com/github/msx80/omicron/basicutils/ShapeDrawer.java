package com.github.msx80.omicron.basicutils;

import com.github.msx80.omicron.api.Sys;

public class ShapeDrawer 
{
	public static interface PointConsumer
	{
		void consumer(int x, int y);
	}

	public static void outline(Sys sys, int x, int y, int w, int h, int sheet, int color)
	{
	
		sys.fill(sheet, x+1, y, w-2, 1, color);
		sys.fill(sheet, x+1, y+h-1, w-2, 1, color);
		sys.fill(sheet, x, y, 1, h, color);
		sys.fill(sheet, x+w-1, y, 1, h, color);
		
	}

	public static void rect(Sys sys, int x, int y, int w, int h, int sheet, int color)
	{
	
		sys.fill(sheet, x, y, w, h, color);
		
	}
	
	
	public static void line(final Sys sys, int x1, int y1, int x2, int y2, final int sheet, final int color) {
		line(x1, y1, x2, y2, new PointConsumer() {
			@Override
			public void consumer(int x, int y) {
				sys.fill(sheet, x, Math.round(y), 1, 1, color);
				
			}
		} );
		
	}
	
	
	public static void line(int x1, int y1, int x2, int y2, PointConsumer pc) {
		int dx = x2-x1;
		int dy = y2-y1;
		if(Math.abs(dx) > Math.abs(dy))
		{
			if(dx>0)
				lineo( x1,y1,x2, dx, dy ,pc);
			else
				lineo( x2,y2,x1, -dx, -dy, pc);
		}
		else
		{
			if(dy>0)
				linev( x1,y1,y2, dx, dy, pc);
			else
				linev( x2,y2,y1, -dx, -dy, pc);
		}
	}

	private static final void lineo( int x1, int y1, int x2, int dx, int dy, PointConsumer pc) {
		float d = ((float)dy) / ((float)dx);
		float y = y1;
		for (int x = x1; x <= x2; x++) {
			pc.consumer(x, Math.round(y));
			y += d;
		}
	}

	private static final void linev(int x1, int y1, int y2, int dx, int dy, PointConsumer pc) {
		float d = ((float)dx) / ((float)dy);
		float x = x1;
		for (int y = y1; y <= y2; y++) {
			pc.consumer(Math.round(x), y);
			x += d;
		}
	}

}
