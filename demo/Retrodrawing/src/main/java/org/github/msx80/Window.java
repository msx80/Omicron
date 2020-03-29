package org.github.msx80;

import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.Sys;

public abstract class Window {
	
	boolean wasEverDown = false;
	
	public abstract void draw(Sys sys, int sheetNum);

	public abstract boolean update(Mouse m);
	
}
