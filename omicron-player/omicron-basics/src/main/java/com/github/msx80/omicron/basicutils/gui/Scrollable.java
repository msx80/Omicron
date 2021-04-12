package com.github.msx80.omicron.basicutils.gui;

/**
 * A widget that can get a "mouse down" and then can be dragged around
 *
 */
public interface Scrollable {
	public void startScroll(int x, int y);
	public void doScroll(int dx, int dy);
	public void endScroll();
}
