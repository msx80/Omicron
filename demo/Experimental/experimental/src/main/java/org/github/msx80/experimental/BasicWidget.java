package org.github.msx80.experimental;

import org.github.msx80.omicron.basicutils.TextDrawer;

public class BasicWidget extends Widget<Pair<String, Runnable>> {

	TextDrawer td;
	
	
	
	public BasicWidget(int x, int y, int w, int h, TextDrawer td) {
		super(x, y, w, h, 8);
		this.td = td;
	}

	@Override
	public void drawBackground(int x, int y) {

	}

	@Override
	public void drawForeground(int x, int y) {

	}

	@Override
	protected boolean selected(int idx, Pair<String, Runnable> line) {
		line.b.run();
		return false;
	}

	@Override
	protected boolean clickedOutside(int x, int y) {
		return false;
	}

	@Override
	public void drawItem(int ax, int ay, int idx, Pair<String, Runnable> line) {
		td.print(line.a, ax, ay);

	}

}
