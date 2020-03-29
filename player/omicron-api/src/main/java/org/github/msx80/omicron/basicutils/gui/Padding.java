package org.github.msx80.omicron.basicutils.gui;

public class Padding {

	public static final Padding ZERO = Padding.of(0);
	
	public final int top;
	public final int bottom;
	public final int left;
	public final int right;
	
	public Padding(int top, int bottom, int left, int right) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public int leftRight()
	{
		return left+right;
	}
	public int topBottom()
	{
		return top+bottom;
	}
	
	public static Padding of(int padding)
	{
		return new Padding(padding, padding, padding, padding);
	}
	public static Padding of(int topBottom, int leftRight)
	{
		return new Padding(topBottom, topBottom, leftRight, leftRight);
	}
	public static Padding of(int top, int bottom, int left, int right) {
		return new Padding(top, bottom, left, right);
	}
	
}
