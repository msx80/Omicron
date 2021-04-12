package com.github.msx80.omicron.basicutils.gui;

import com.github.msx80.omicron.api.Sys;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.ShapeDrawer;

public class TestArea extends Widget {

	private Sys s;

	public TestArea(Sys s, int w, int h) {
		super(w, h);
		this.s = s;
	}

	@Override
	public void draw() {
		ShapeDrawer.rect(s, 0, 0, w, h, 0, Colors.BLUE);
		ShapeDrawer.outline(s, 0, 0, w, h, 0, Colors.RED);

	}

}
