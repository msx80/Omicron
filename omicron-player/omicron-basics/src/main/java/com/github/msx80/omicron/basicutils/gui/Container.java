package com.github.msx80.omicron.basicutils.gui;

import com.github.msx80.omicron.api.Sys;

import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.palette.Tic80;

public class Container extends ManagedParentWidget {

	protected Sys sys;
	
	public Container(Sys sys)
	{
		super(sys, 0, 0);
		this.sys = sys;
	}
	
	
	@Override
	public void draw() {
		sys.fill(0, 0, 0, w, h, Tic80.BLUE_GRAY);
		ShapeDrawer.outline(sys, 0, 0, w, h, 0, Tic80.BROWN);
		
		drawChildren();
	}

	@Override
	protected void childInvalidated(Widget widget) {
		int mw = 0;
		int mh = 0;
		for (Widget w : children()) {
			int cx = w.getX() + w.getW();
			int cy = w.getY() + w.getH();
			if (cx>mw) {
				mw = cx;
			}
			if (cy>mh) {
				mh = cy;
			}
		}
		this.w = mw;
		this.h = mh;
		invalidate();
	}




}
