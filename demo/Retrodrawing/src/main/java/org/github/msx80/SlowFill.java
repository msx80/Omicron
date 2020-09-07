package org.github.msx80;

import java.util.ArrayList;
import java.util.List;

import org.github.msx80.omicron.api.Pointer;
import org.github.msx80.omicron.api.Sys;

public class SlowFill implements Tool {


	
	boolean wasDown = false;
	

	List<Pair<Integer, Integer>> z = null;
	private int origColor;
	private int newColor;
	
	public SlowFill() {
	
	}


	private void fill(int origColor, int newColor, Sys sys, int surface)
	{
		
			if(z.isEmpty()) 
			{
				z = null;
				return;
			}
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

	@Override
	public void update(Ctx ctx, Pointer m) {
		Sys sys = ctx.getSys();
		int surface = ctx.getSurface();
		if(z!=null)
		{
			fill(origColor, newColor, sys, surface);
			return;
		}
		if(m.btn(0)) 
		{
			if(m.x()<RetroDrawer.SURFWIDTH)
			{
				if(!wasDown)
				{
					
					origColor = sys.getPix( surface, m.x(), m.y());
					newColor = Palette.P[ctx.currentColor()];
					if(newColor!=origColor)
					{
						ctx.recordUndo();
						z = new ArrayList<Pair<Integer,Integer>>();
						z.add(Pair.of(m.x(),  m.y()));
						fill(origColor, newColor,  sys, surface);
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
		
		return z!=null;
	}

}
