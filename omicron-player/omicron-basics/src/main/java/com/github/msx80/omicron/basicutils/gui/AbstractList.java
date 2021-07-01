package com.github.msx80.omicron.basicutils.gui;

public abstract class AbstractList<T> extends BaseWidget implements PartialDrawable, Clickable {

	public AbstractList(int w, int h) {
		super(w, h);
		
	}

	public abstract int itemsCount();

	public abstract void clicked(int idx);
}
