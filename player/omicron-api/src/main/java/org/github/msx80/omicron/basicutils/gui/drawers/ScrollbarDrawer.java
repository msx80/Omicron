package org.github.msx80.omicron.basicutils.gui.drawers;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.ShapeDrawer;

public class ScrollbarDrawer implements IScrollbarDrawer {

	int width;
	int curColor;
	int outlineColor;
	
	public ScrollbarDrawer(int width, int curColor, int outlineColor) {
		this.width = width;
		this.curColor = curColor;
		this.outlineColor = outlineColor;
	}

	public int getThickness()
	{
		return width;
	}
	
	public void drawHorizontalScrollbar(Sys sys, int sx, int sy, int sw, int curPos, int curLen) {
		sys.fill(0, sx+curPos, sy, curLen, getThickness(), curColor);
		ShapeDrawer.outline(sys, sx, sy,sw, getThickness(), 0, outlineColor);
	}

	public void drawVerticalScrollbar(Sys sys, int sx, int sy, int sh, int curPos, int curLen) {
		sys.fill(0, sx, sy+curPos, getThickness(), curLen, curColor);
		ShapeDrawer.outline(sys,sx, sy, getThickness(), sh, 0, outlineColor);
	}

	@Override
	public int getBorder() {
		return 1;
	}

}
