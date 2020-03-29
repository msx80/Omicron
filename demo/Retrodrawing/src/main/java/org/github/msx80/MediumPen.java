package org.github.msx80;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.ShapeDrawer.PointConsumer;

public class MediumPen extends Pen {



	public void dotPen(Ctx ctx, int x, int y) 
	{
		Sys sys = ctx.getSys();
		int surface = ctx.getSurface();
		
		sys.fill(surface, x-2, y-1, 4,2, Palette.P[ctx.currentColor()]);
		sys.fill(surface, x-1, y-2, 2,4, Palette.P[ctx.currentColor()]);
	}



	@Override
	public void linePen(Ctx ctx, int x, int y, int x2, int y2) {
		
		ShapeDrawer.line(x, y, x2, y2, new PointConsumer() {
			
			@Override
			public void consumer(int x, int y) {
				
				dotPen(ctx, x,y);
			}
		});
	}

}
