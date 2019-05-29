package org.github.msx80.omicron;

import com.badlogic.gdx.Gdx;

public class DebugPlugin implements HardwarePlugin {

	@Override
	public String exec(String command, String params) {
		
		return "RES: "+Gdx.graphics.getWidth()+"x"+Gdx.graphics.getHeight();
	}

	@Override
	public String name() {
		
		return "DEBUG";
	}

}
