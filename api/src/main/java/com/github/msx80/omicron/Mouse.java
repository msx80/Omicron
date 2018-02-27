package com.github.msx80.omicron;

public interface Mouse {
	
	int x();
	int y();
	boolean wasPressed(int button);
	boolean wasReleased(int button);
	boolean isPressed(int button);
}
