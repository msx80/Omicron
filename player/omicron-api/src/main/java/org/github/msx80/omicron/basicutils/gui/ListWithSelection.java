package org.github.msx80.omicron.basicutils.gui;


import org.github.msx80.omicron.basicutils.TextDrawer;

public class ListWithSelection<T> extends Widget {

	private java.util.List<T> items;
	private TextDrawer td;
	
	private int selected = 0;
	
	public ListWithSelection(java.util.List<T> items, TextDrawer td, int x, int y, int w, Padding padding) {
		super(x, y, w, items.size() * (td.height()+1), padding);
		this.items = items;
		this.td = td;
	}

	@Override
	public void draw() {
		int yy = 0;
		int num = 0;
		for (T t : items) {
			String s = (selected == num ? "> ":"  ")+t.toString(); 
			td.print(s, 0, yy);
			yy+=td.height()+1;
			num++;
		}
	}

	public int getSelected() {
		return selected;
	}

	public void select(int i) {
		selected = i;
		if(selected>=items.size())
		{
			selected = items.size()-1;
		}
		if (selected<0) {
			selected = 0;
		}
		if(parent!=null) parent.ensureVisible(this, 0, selected*(td.height()+1), this.w, td.height()+1);
	}

}
