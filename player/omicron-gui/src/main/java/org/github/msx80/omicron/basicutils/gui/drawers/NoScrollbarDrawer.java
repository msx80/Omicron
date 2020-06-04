package org.github.msx80.omicron.basicutils.gui.drawers;

import org.github.msx80.omicron.api.Sys;

public class NoScrollbarDrawer implements ScrollbarDrawer {

	@Override
	public int getThickness() {
		return 0;
	}

	@Override
	public int getBorder()
	{
		return 0;
	}
	
	@Override
	public void drawHorizontalScrollbar(Sys sys, int sx, int sy, int sw, int curPos, int curLen) {
	}

	@Override
	public void drawVerticalScrollbar(Sys sys, int sx, int sy, int sh, int curPos, int curLen) {
	}

}
