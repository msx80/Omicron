package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;

public class WidgetManager extends ManagedParentWidget {

	public WidgetManager(Sys sys) {
		super(sys, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public void childInvalidated(Widget widget) {
		// nothing to do here
	}
	
	public void propagateclick(int px, int py)
	{
		Widget w = pick(px, py);
		// if we found any but it's not clickable,
		// climb the hyerarchy until we find a clickable parent
		while ( (w != null) && !(w instanceof Clickable))
		{
			w = w.parent;
			px = px+w.x;
			py = py+w.y;
		}

		if(w!=null)
		{
			((Clickable) w).click(px-w.x, py-w.y);
		}
	}
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// nothing to do here
		
	}

	public void update() {
		// if clicked handle click
		
		handleControllables(this);
		
	}

	private void handleControllables(Widget w) {
		if(w instanceof Controllable)
		{
			((Controllable) w).control(sys.controllers()[0]);
		}
		if(w instanceof ParentWidget)
		{
			for (Widget c : ((ParentWidget) w).children()) {
				handleControllables(c);
			}
		}
		
	}

	

}
