package org.github.msx80;

import org.github.msx80.omicron.api.Mouse;

public abstract class Pen implements Tool {

	boolean wasDown = false;
	
	int ox = 0;
	int oy = 0;


	public abstract void dotPen(Ctx ctx, int x, int y);
	public abstract void linePen(Ctx ctx, int x, int y, int x2, int y2);


	@Override
	public void update(Ctx ctx, Mouse m) {
		if(m.btn(0)) 
		{
			if(m.x()<RetroDrawer.SURFWIDTH)
			{
				if(wasDown)
				{
					//ShapeDrawer.line(sys, m.x, m.y, ox, oy, surface, Palette.P[ctx.currentColor()]);
					linePen(ctx, m.x(), m.y(), ox, oy);
				}
				else
				{
					ctx.recordUndo();
					dotPen(ctx, m.x(),m.y());
					//sys.fill(surface, m.x, m.y, 1,1, Palette.P[ctx.currentColor()]);
				}
				ox = m.x();
				oy = m.y();
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
