package org.github.msx80.omicron.basicutils.gui;

import java.util.List;

import org.github.msx80.omicron.api.Sys;


/**
 * A widget that has some child widgets.
 * It should draw the widget accordingly, handle the "pick" picking, etc.
 * Note that there's no add() method to add children as this is an implementation specific operation that
 * different parents can implement differently 
 *
 */
public abstract class ParentWidget extends Widget {

	protected Sys sys;
	
	public ParentWidget(Sys sys, int x, int y, int w, int h, Padding padding) {
		super(x, y, w, h, padding);
		this.sys = sys;
	}

	public void drawChildren() {
		sys.offset(padding.top, padding.left);
		for (Widget w : children()) {
			sys.offset(w.x, w.y);
			w.draw();
			sys.offset(-w.x, -w.y);
		}
		sys.offset(-padding.top, -padding.left);
	}
	
	@Override
	public void draw() {
		drawChildren();
	}
	
	public abstract void childInvalidated(Widget widget);

	public abstract List<Widget> children();
		
	public Widget pick(int px, int py)
	{
		for (int i = children().size() -1 ; i >= 0 ; i--) {
			Widget w = children().get(i);
			if(w.isInside(px, py))
			{
				if(w instanceof ParentWidget)
				{
					return ((ParentWidget) w).pick(px-w.x-w.padding.top, py-w.y-w.padding.bottom);
				}
				else
				{
					return w;
				}
			}
		}
		return this;
	}

	/**
	 * For scrolling or partially visible parents, ensure the visible area of the child includes the specified area
	 */
	public void ensureVisible(Widget child, int x, int y, int w, int h) {

		parent.ensureVisible(this, child.x+x+padding.top, child.y+y+padding.left, w, h);
		
	}

	
}
