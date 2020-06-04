package org.github.msx80.omicron.basicutils.gui.drawers;

import org.github.msx80.omicron.api.Sys;

public interface ScrollbarDrawer {

	public int getThickness();

	public int getBorder();
	
	public void drawHorizontalScrollbar(Sys sys, int sx, int sy, int sw, int curPos, int curLen);

	public void drawVerticalScrollbar(Sys sys, int sx, int sy, int sh, int curPos, int curLen);


}
