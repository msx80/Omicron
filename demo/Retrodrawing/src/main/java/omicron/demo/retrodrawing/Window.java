package omicron.demo.retrodrawing;

import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.Sys;

public abstract class Window {
	
	boolean wasEverDown = false;
	
	public abstract void draw(Sys sys, int sheetNum);

	public abstract boolean update(Pointer m);
	
}
