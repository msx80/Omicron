package org.github.msx80;

import java.util.function.Consumer;

import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;

public class ColorWindow extends Window {

	private static final int CELLSIZE = 20;
	private static final int boxWidth = CELLSIZE*8+2;
	private static final int boxHeight = CELLSIZE*4+2;

	private static final int ax = (RetroDrawer.SURFWIDTH - boxWidth) / 2;
	private static final int ay = (RetroDrawer.HEIGHT - boxHeight) / 2;
	private Consumer<Integer> onColorChoose;
	
	public ColorWindow(Consumer<Integer> onColorChoose) {
		this.onColorChoose = onColorChoose;
	}
	
	
	
	public void draw(Sys sys, int sheetNum)
	{
		sys.fill(sheetNum, ax, ay, CELLSIZE*8+2, CELLSIZE*4+2, Colors.BLACK);
		for (int i = 0; i < Palette.P.length; i++) {
			int x = (i % 8) * CELLSIZE;
			int y = (i / 8) * CELLSIZE;
			
			sys.fill(sheetNum, ax+x+1, ay+y+1, CELLSIZE, CELLSIZE, Palette.P[i]);
		}
	}

	public boolean update(Mouse m)
	{
		if (m.btn[0])
		{
			int dx = m.x - ax;
			int dy = m.y - ay;
			if(dx > 0 && dy > 0 && dx<boxWidth-1 && dy <boxHeight-1)
			{
				// choose a color
				dx = (dx-1 )/ CELLSIZE;
				dy = (dy-1) / CELLSIZE;
				int idx = dy*8+dx;
				onColorChoose.accept(idx);
			}
			
			return false;
		}
		
		return true;
	}
	
}
