package com.github.msx80.runtime.implementation;

import com.github.msx80.omicron.Mouse;

public final class MouseImpl implements Mouse {

	int x,y;
	boolean[] pressed = new boolean[5];
	boolean[] isDown = new boolean[5];
	boolean[] released = new boolean[5];
	
	public MouseImpl() {
		resetButtons();
		for (int i = 0; i < isDown.length; i++) {
			isDown[i] = false;
		}
	}

	@Override
	public boolean wasPressed(int button) {
		
		return pressed[button];
	}

	@Override
	public boolean wasReleased(int button) {

		return released[button];
	}
	@Override
	public int x() {
		return x;
	}

	@Override
	public int y() {
		return y;
	}
	public void resetButtons()
	{
		for (int i = 0; i < pressed.length; i++) {
			pressed[i] = false;
			released[i] = false;
		}
	}

	public void btnDown(int x2, int y2, int button) {
		this.x =x2;
		this.y =y2;
		pressed[button] = true;
		isDown[button] = true;
	}

	public void btnUp(int x2, int y2, int button) {
		this.x =x2;
		this.y =y2;
		released[button] = true;
		isDown[button] = false;
		
	}
	
	@Override
	public boolean isPressed(int button)
	{
		return isDown[button];
				
	}
}
