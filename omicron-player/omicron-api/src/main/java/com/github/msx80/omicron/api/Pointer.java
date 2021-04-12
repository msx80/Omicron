package com.github.msx80.omicron.api;

/**
 * Represent either a mouse or a touch screen input.
 * 
 *
 */
public interface Pointer {
	/**
	 * Gets the X coordinate of the pointer, already translate into pixel space
	 * @return
	 */
	int x();
	/**
	 * Gets the Y coordinate of the pointer, already translate into pixel space
	 * @return
	 */
	int y();
	/**
	 * Return if a button is currently pressed.
	 * @param n a value between 0 and 4, where 0 1 and 2 are mouse buttons, 3 and 4 are mouse wheel (up and down). For touch display, only the first button is reported, the others stay false.
	 * @return
	 */
	public boolean btn(int n);
	/**
	 * Return if any of the buttons are currently pressed but weren't pressed the previous loop (so any
	 * press is reported just once no matter how long).
	 */
	public boolean btnp(int n);
}
