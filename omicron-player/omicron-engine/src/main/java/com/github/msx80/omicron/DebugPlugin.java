package com.github.msx80.omicron;

import com.badlogic.gdx.Gdx;
import com.github.msx80.omicron.api.Sys;

public class DebugPlugin implements HardwarePlugin {

	@Override
	public Object exec(String command, Object params) {
		
		return "RES: "+Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight();
	}

	@Override
	public void init(Sys sys, HardwareInterface hw) {
	}

}
