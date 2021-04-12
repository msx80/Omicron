package com.github.msx80.omicron.basicutils.gui;


import com.github.msx80.omicron.api.Controller;
import com.github.msx80.omicron.api.Sys;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.ShapeDrawer;
import com.github.msx80.omicron.basicutils.TextDrawer;

public class Windimation<T> extends Widget implements Controllable {

	private int highlightColor = Colors.from(30, 30, 30);

	public static interface Selector<T>{
		void select(int pos, T item );
	}
	public static interface ItemDrawer<T>
	{
		void draw(T item, int x, int y);
	}
	public static interface CursorDrawer
	{
		void draw(int x, int y);
	}
	private java.util.List<T> items;
	
	private int position = 0;
	private int selected = 0;
	private Sys sys;
	private Selector<T> onSelect;
	
	private CursorDrawer cursorDrawer;
	private ItemDrawer<T> itemDrawer;
	private int itemHeight;
	
	public Windimation(java.util.List<T> items, Selector<T> onSelect, Sys sys, final TextDrawer td, int w) {
		super( w, items.size() * td.height());
		this.sys = sys;
		this.items = items;
		this.itemHeight = td.height();
		this.onSelect = onSelect;
		this.itemDrawer = (t, xx, yy) -> {
			String s = "  "+t.toString(); 
			td.print(s, xx, yy);
		};
		this.cursorDrawer = (xx, yy) -> {
			td.print(">", xx, yy+1);
		};
 	}

	public Windimation(java.util.List<T> items, Selector<T> onSelect, Sys sys, ItemDrawer<T> itemDrawer, CursorDrawer cursorDrawer, int itemHeight, int w) {
		super(w, items.size() * itemHeight);
		this.sys = sys;
		this.items = items;
		this.itemHeight = itemHeight;
		this.onSelect = onSelect;
		this.itemDrawer = itemDrawer;
		this.cursorDrawer = cursorDrawer;
 	}

	@Override
	public void draw() {
		int yy = 0;
		int num = 0;
		for (T t : items) {
			if(num == selected) ShapeDrawer.rect(sys, 0, num*itemHeight, this.w, itemHeight, 0, highlightColor);
			itemDrawer.draw(t, 0, yy);
			yy+=itemHeight;
			num++;
		}
		cursorDrawer.draw(0, position);
		
	}

	public int getSelected() {
		return selected;
	}
	@Override
	public void control(Controller c) {
		if(c.down())
		{
			updatePosition(1);
		} else if (c.up())
		{
			updatePosition(-1);
		}
		if(c.btnp(0))
		{
			onSelect.select(selected, items.get(selected));
		}
	}
	
	

	public void setCursorDrawer(CursorDrawer cursorDrawer) {
		this.cursorDrawer = cursorDrawer;
	}

	public void setItemDrawer(ItemDrawer<T> itemDrawer) {
		this.itemDrawer = itemDrawer;
	}

	private void updatePosition(int i) {
		position += i;
		if (position> this.h - itemHeight) {
			position = this.h - itemHeight;
		}
		if (position<0)
		{
			position = 0;
		}
		selected = (position+itemHeight/2) / itemHeight;
		
		if(parent!=null) parent.ensureVisible(this, 0, position, this.w, itemHeight);
	}

	public int getHighlightColor() {
		return highlightColor;
	}

	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	public int getItemHeight() {
		return itemHeight;
	}

	public void setItemHeight(int itemHeight) {
		this.itemHeight = itemHeight;
		this.invalidate(); // size changed, notify parent
	}
	
	

}
