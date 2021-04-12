package omicron.demo.retrodrawing;

import com.github.msx80.omicron.basicutils.ShapeDrawer;

public class SmallPen extends Pen {
	

	@Override
	public void dotPen(Ctx ctx, int x, int y) {
		ctx.getSys().fill(ctx.getSurface(), x, y, 1,1, Palette.P[ctx.currentColor()]);
	}

	@Override
	public void linePen(Ctx ctx,int x, int y, int x2, int y2) {
		ShapeDrawer.line(ctx.getSys(), x, y, x2, y2, ctx.getSurface(), Palette.P[ctx.currentColor()]);
	}

}
