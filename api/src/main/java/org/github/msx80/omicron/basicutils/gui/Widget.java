package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.basicutils.Geometry;

public abstract class Widget 
{
	protected ParentWidget parent = null;
	protected int x;
	protected int y;
	protected int w;
	protected int h;
	protected Padding padding;
	
	public Widget(int x, int y, int w, int h, Padding padding) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.padding = padding;
	}

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

	protected abstract void click(int px, int py);
	
	
	public int getAbsoluteX()
	{
		return x+(parent == null ? 0 : (parent.getPadding().left+ parent.getAbsoluteX()));
	}
	
	public int getAbsoluteY()
	{
		return y+(parent == null ? 0 : (parent.getPadding().top + parent.getAbsoluteY()));
	}
	
	public Padding getPadding()
	{
		return padding;
	}
	
}
