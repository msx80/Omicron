package com.github.msx80.omicron.basicutils.gui;


import com.github.msx80.omicron.basicutils.TextDrawer;

public class List<T> extends AbstractList<T> {

	private java.util.List<T> items;
	private TextDrawer td;
	
	public List(java.util.List<T> items, TextDrawer td, int w) {
		super(w);
		this.items = items;
		this.td = td;
		init();
	}

	@Override
	public void drawItem(int idx, int x, int y, boolean odd) {
		
		td.print(items.get(idx).toString(), x, y);
	}

	@Override
	public int itemsHeight() {
		return td.height()+1;
	}

	@Override
	public int itemsCount() {
		return items.size();
	}

}
