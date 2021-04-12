package com.github.msx80.omicron.basicutils.gui;

/**
 * Indicates what a widget want to receive click events
 * If a widget doesn't implement this interface and is clicked,
 * then the first parent to implement it will receive the click.
 */
public interface Clickable {
	void click(int px, int py);	
}
