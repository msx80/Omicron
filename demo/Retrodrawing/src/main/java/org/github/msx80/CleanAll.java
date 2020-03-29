package org.github.msx80;

public class CleanAll implements ClickyTool {


	@Override
	public void execute(Ctx ctx) {
		ctx.recordUndo();
		ctx.getSys().fill(ctx.getSurface(), 0,0, RetroDrawer.SURFWIDTH, RetroDrawer.HEIGHT, Palette.P[0]);
		
	}


}
