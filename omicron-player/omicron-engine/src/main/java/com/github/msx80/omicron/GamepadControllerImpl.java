package com.github.msx80.omicron;

import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.ControllerPowerLevel;
import com.github.msx80.omicron.api.Controller;

public class GamepadControllerImpl implements Controller, ControllerListener {

	
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;
	public boolean[] btn = new boolean[] {false, false, false, false};
	public boolean[] oldbtn = new boolean[] {false, false, false, false};
	private com.badlogic.gdx.controllers.Controller controller;
	
	public GamepadControllerImpl(com.badlogic.gdx.controllers.Controller controller) {
		this.controller = controller;
		controller.addListener(this);
	}
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
	
	private char b(boolean b) {
		return b ? '+':'-';
	}


	@Override
	public String toString() {
		String s =  "btn=";
		for (int i = 0; i < btn.length; i++) {
			s += b(btn[i]);
		}
		s+= " U" + b(up) + "D" + b(down) + "L" + b(left) + "R" + b(right);
		
		return s;
	}
	
	
	@Override
	public void connected(com.badlogic.gdx.controllers.Controller controller) {
		
		
	}

	@Override
	public void disconnected(com.badlogic.gdx.controllers.Controller controller) {
		
		
	}

	@Override
	public boolean buttonDown(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
		if(buttonCode < 4) btn[buttonCode] = true;
		return true;
	}

	@Override
	public boolean buttonUp(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
		System.out.println("btn code: "+buttonCode);
		if(buttonCode < 4) btn[buttonCode] = false;
		return false;
	}

	@Override
	public boolean axisMoved(com.badlogic.gdx.controllers.Controller controller, int axisCode, float value) {
		System.out.println("Axis code: "+axisCode+" val "+value);
		if(axisCode == 1)
		{
			if(value > 0.5) { down = true; up = false;}
			else if(value < -0.5) { down = false; up = true;}
			else {up = false; down = false;}
		} else if(axisCode == 0)
		{
			if(value > 0.5) { right = true; left = false;}
			else if(value < -0.5) { right = false; left = true;}
			else {left = false; right = false;}
		}

		return false;
	}


}
