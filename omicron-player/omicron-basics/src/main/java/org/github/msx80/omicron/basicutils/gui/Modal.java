package org.github.msx80.omicron.basicutils.gui;

import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;


/**
 * A container open modally, ie it prevents interaction with the rest
 * of the widgets until it is removed.
 * It works by exploiting the fact that the last widget added is the first
 * processed.
 *
 */
public class Modal extends ManagedParentWidget implements Clickable
{
	

	public Modal(Sys sys, int screenWidth, int screenHeight) {
		super(sys, screenWidth, screenHeight);
	}

	@Override
	public boolean isInside(int px, int py) {
		// a modal is always fullscreen
		return true;
	}

	@Override
	public void childInvalidated(Widget widget) {
		// nothing to do here
	}

	@Override
	public void draw() {
		sys.color(Colors.from(0, 0, 0, 64));
		sys.fill(0, 0, 0, w, h, Colors.WHITE);
		sys.color(Colors.WHITE);
		super.draw();
	}
	
	public void close()
	{
		if(parent!=null)
		{
			parent.remove(this);
		}
	}

	@Override
	public void click(int px, int py) {
		this.close();
	}
}
