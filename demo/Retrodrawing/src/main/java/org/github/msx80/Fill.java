package org.github.msx80;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;

public class Fill implements Tool {

	
	boolean wasDown = false;
	

	private void fill(int origColor, int newColor, List<Pair<Integer, Integer>> z, Sys sys, int surface)
	{
		while(true)
		{
			if(z.isEmpty()) return;
			List<Pair<Integer, Integer>> newlist = new ArrayList<Pair<Integer,Integer>>();
			
			for (Pair<Integer, Integer> co : z) {
			
				int x = co.a;
				int y = co.b;
			
				if(x<0 || y<0 || x>=RetroDrawer.SURFWIDTH || y>= RetroDrawer.HEIGHT) continue;
				
				int c = sys.getPix(surface, x, y);
				if(c == origColor)
				{
					sys.fill(surface, x, y, 1, 1, newColor);
					newlist.add(Pair.of(x+1, y));
					newlist.add(Pair.of(x-1, y));
					newlist.add(Pair.of(x, y+1));
					newlist.add(Pair.of(x, y-1));
				}
			}
			
			z = newlist;
		}
	}

	@Override
	public void update(Ctx ctx, Mouse m) {
		if(m.btn[0]) 
		{
			if(m.x<RetroDrawer.SURFWIDTH)
			{
				if(!wasDown)
				{
					int origColor = ctx.getSys().getPix(ctx.getSurface(), m.x, m.y);
					int newColor = Palette.P[ctx.currentColor()];
					if(newColor!=origColor)
					{
						ctx.recordUndo();
						List<Pair<Integer, Integer>> a = new ArrayList<Pair<Integer,Integer>>();
						a.add(Pair.of(m.x,  m.y));
						fill(origColor, newColor, a, ctx.getSys(), ctx.getSurface());
					}
				}
			}
			wasDown = true;
			
		}
		else
		{
			wasDown = false;
			
		}
	}


	@Override
	public boolean isBusy() {
		return false;
	}

}
