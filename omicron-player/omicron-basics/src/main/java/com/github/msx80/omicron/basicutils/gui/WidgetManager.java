package com.github.msx80.omicron.basicutils.gui;

import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.Sys;

public class WidgetManager extends ManagedParentWidget {

	
	Scrollable scrolling;   // widget currently being scrolled
	int scrollpx;   		// scroll start position		  
	int scrollpy; 
	int scrollTotal;	 	// total amount scrolled, to decide wether to consider it a click or a scroll
	
	public WidgetManager(Sys sys) {
		super(sys, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public void childInvalidated(Widget widget) {
		// nothing to do here
	}
	
	public void propagateclick(Widget from, int px, int py)
	{
		Widget w = from.find(px, py, true, a -> a instanceof Clickable);

		if(w!=null)
		{
			
			((Clickable) w).click(px-w.getAbsoluteX(), py-w.getAbsoluteY());
		}
	}
	
	
	@Override
	public void ensureVisible(Widget child, int x, int y, int w, int h) {
		// nothing to do here
		
	}

	public void update() {
		
		Pointer mouse = sys.pointers()[0];
		int mx = mouse.x();
		int my = mouse.y();
		
		if(scrolling != null)
		{
			Widget ws = (Widget) scrolling;
			if(!mouse.btn(0))
			{
				scrolling.endScroll();
				scrolling = null;
				if(scrollTotal<4)
				{
					// scrolled too little, consider it a click instead
					propagateclick(this, mx, my);
				}
			}
			else
			{
				// still scrolling
				int dx = mx - scrollpx;
				int dy = my - scrollpy;
				if(dx != 0 || dy != 0)
				{
					scrolling.doScroll(dx, dy, mx-ws.getAbsoluteX(), my-ws.getAbsoluteY());
					scrollpx = mx;
					scrollpy = my;
					scrollTotal += Math.abs(dx) + Math.abs(dy);
				}
			}
		}
		else
		{
			if(mouse.btnp(0))
			{
				scrolling = (Scrollable) find(mx, my, false, w -> w instanceof Scrollable);
				if(scrolling != null)
				{
					Widget ws = (Widget) scrolling;
					scrollpx = mx;
					scrollpy = my;
					scrollTotal = 0;
					scrolling.startScroll(mx-ws.getAbsoluteX(), my-ws.getAbsoluteY());
				}
				else
				{
					propagateclick(this, mx, my);
				}
			}
		}
		
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
