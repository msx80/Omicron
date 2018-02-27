package com.github.msx80.omicron;

public interface Controller 
{
	public enum Direction {
		center, north, south, east, west, northEast, southEast, northWest, southWest
	}
	Direction dir();
	boolean up();
	boolean down();
	boolean left();
	boolean right();
	boolean wasPressed(int button);
	boolean wasReleased(int button);
	boolean isPressed(int button);
	
	String name();
}
