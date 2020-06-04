package org.github.msx80.omicron;

import org.github.msx80.omicron.api.Mouse;

public class MouseImpl implements Mouse {

	private int x;
	private int y;
	
	protected boolean[] btn = new boolean[] {false, false, false};
	protected boolean[] oldbtn = new boolean[] {false, false, false};
	
	
	void set(int ax, int ay)
	{
		x = ax;
		y = ay;
	}
		
	@Override
	public int x() {
		return x;
	}
	@Override
	public int y() {
		return y;
	}
	@Override
	public boolean btn(int n) {
		return btn[n];
	}
	@Override
	public boolean btnp(int n) {
		return btn[n] && !oldbtn[n];
	}	
	public void copyOld() {
		System.arraycopy(btn, 0, oldbtn, 0, btn.length);
		
	}
}
