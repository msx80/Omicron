package org.github.msx80.omicron;

import org.github.msx80.omicron.api.Sys;

import com.badlogic.gdx.Gdx;

public class DebugPlugin implements HardwarePlugin {

	@Override
	public Object exec(String command, Object params) {
		
		return "RES: "+Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight();
	}

	@Override
	public String name() {
		
		return "DEBUG";
	}

	@Override
	public void init(Sys sys, HardwareInterface hw) {
	}

}
