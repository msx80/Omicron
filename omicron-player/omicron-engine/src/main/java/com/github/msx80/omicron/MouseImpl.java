package com.github.msx80.omicron;

import java.util.Arrays;

import com.github.msx80.omicron.api.Pointer;

public class MouseImpl implements Pointer {

	public int x = 0;
	public int y = 0;
	
	// button 3 and 4 are for the scrollwheel
	public boolean[] btn = new boolean[] {false, false, false, false, false};
	public boolean[] oldbtn = new boolean[] {false, false, false, false, false};
	
	
	public void set(int ax, int ay)
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
	
	private char b(boolean b) {
		return b ? '+':'-';
	}


	@Override
	public String toString() {
		String s =  "btn=";
		for (int i = 0; i < btn.length; i++) {
			s += b(btn[i]);
		}
		s+= " x=" + x + " y=" + y;
		
		return s;
	}

	public void resetScroll() {
		btn[3] = false;
		btn[4] = false;
	}

	public void resetButtons() {
		Arrays.fill(btn, false);
	}
	
	
	
}
