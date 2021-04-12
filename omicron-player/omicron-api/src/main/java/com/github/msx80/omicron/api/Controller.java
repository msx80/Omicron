package com.github.msx80.omicron.api;

public interface Controller 
{
	boolean up();
	boolean down();
	boolean left();
	boolean right();
	public boolean btn(int n);
	public boolean btnp(int n);
}
