package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;

public class WidgetManager extends ManagedParentWidget {

	public WidgetManager(Sys sys) {
		super(sys, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, Padding.ZERO);
	}

	@Override
	public void childInvalidated(Widget widget) {
		// nothing to do here
	}
	
	public void propagateclick(int px, int py)
	{
		Widget w = pick(px, py);
		if(w!=null)
		{
			if (w instanceof Clickable)	((Clickable) w).click(px-w.x, py-w.y);
		}
	}
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// nothing to do here
		
	}

	

}
