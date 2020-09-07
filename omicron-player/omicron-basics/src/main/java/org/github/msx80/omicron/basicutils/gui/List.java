package org.github.msx80.omicron.basicutils.gui;


import org.github.msx80.omicron.basicutils.TextDrawer;

public class List<T> extends Widget {

	private java.util.List<T> items;
	private TextDrawer td;
	
	public List(java.util.List<T> items, TextDrawer td, int x, int y, int w) {
		super(x, y, w, items.size() * (td.height()+1));
		this.items = items;
		this.td = td;
	}

	@Override
	public void draw() {
		int yy = 0;
		for (T t : items) {
			td.print(t.toString(), 0, yy);
			yy+=td.height()+1;
		}
	}

}
