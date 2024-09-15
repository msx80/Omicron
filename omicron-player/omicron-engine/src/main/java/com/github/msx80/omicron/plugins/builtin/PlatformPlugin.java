package com.github.msx80.omicron.plugins.builtin;

import com.badlogic.gdx.Gdx;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class PlatformPlugin implements HardwarePlugin {

	@Override
	public void init(HardwareInterface hw) {

	}

	@Override
	public Object exec(String command, Object params) {
		if("PLATFORM".equals(command))
		{
			return Gdx.app.getType().toString().toUpperCase();
		}
		return "ERR: command not found";
	}

}
