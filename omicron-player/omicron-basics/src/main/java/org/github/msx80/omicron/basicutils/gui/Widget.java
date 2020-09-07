package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.basicutils.Geometry;


/**
 * Basic Widget based toolkit.
 * Architecture:
 *  each Widget controls his dimensions and position (relative to the parent)
 *   
 *   x,y: position *relative to the parent*, to be added to the parent padding when drawing. (so if x=3 and parent padding = 2, then it will be drawn at 5)
 *   w,h: width and height.
 *   
 *   When any of x,y,w,h are changed, it should call the invalidate() method of the widget, which propagates the notification upward to parents. 
 *   This can be used for example to implement an elastic container which grows or shrink following children resize.
 *   
 *  The draw method already set up the view so that 0,0 is at the top left corner of the widget.
 *   
 * @author msx80
 *
 */
public abstract class Widget 
{
	protected ParentWidget parent = null;
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	public Widget(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Draw the content of the widget. The view is already offsetted so that the top left corner is at 0,0, so a widget could
	 * 1) draw the background filling 0,0,w,h
	 * 2) draw the content at padding.left, padding,top (up to padding.right, padding.bottom) 
	 */
	public abstract void draw();
	
	public void invalidate()
	{
		if(parent!=null) parent.childInvalidated(this);
	}
	
	protected void setParent(ParentWidget parent)
	{
		this.parent = parent;
	}
	
	public boolean isInside(int px, int py)
	{
		return Geometry.inRect(px, py, x, y, w, h);
	}

	
	
	
	/**
	 * Return the coordinate X of the widget in screen space, walking back the parent hyerarchy, handling positions and paddings 
	 * @return
	 */
	public int getAbsoluteX()
	{
		return x+(parent == null ? 0 : parent.getAbsoluteX());
	}
	
	/**
	 * Return the coordinate Y of the widget in screen space, walking back the parent hyerarchy, handling positions and paddings 
	 * @return
	 */
	public int getAbsoluteY()
	{
		return y+(parent == null ? 0 : parent.getAbsoluteY());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	
	
	
}
