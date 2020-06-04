package org.github.msx80.omicron;

import org.github.msx80.omicron.api.Controller;

public class ControllerImpl implements Controller
{
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;
	public boolean[] btn = new boolean[] {false, false, false, false};
	public boolean[] oldbtn = new boolean[] {false, false, false, false};
	
	@Override
	public boolean up() {
		return up;
	}
	@Override
	public boolean down() {
		return down;
	}
	@Override
	public boolean left() {
		return left;
	}
	@Override
	public boolean right() {
		return right;
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
