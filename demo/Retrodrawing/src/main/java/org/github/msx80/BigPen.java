package org.github.msx80;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.ShapeDrawer;
import org.github.msx80.omicron.basicutils.ShapeDrawer.PointConsumer;

public class BigPen extends Pen {




	public void dotPen(Ctx ctx, int x, int y) 
	{
		Sys sys = ctx.getSys();
		int surface = ctx.getSurface();
		sys.fill(surface, x-4, y-4, 8,8, Palette.P[ctx.currentColor()]);
		sys.fill(surface, x-5, y-3, 10,6, Palette.P[ctx.currentColor()]);
		sys.fill(surface, x-3, y-5, 6,10, Palette.P[ctx.currentColor()]);
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
