package com.github.msx80.omicron.plugins.builtin;

import com.badlogic.gdx.Gdx;
import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class UrlOpenerPlugin implements HardwarePlugin {

	@Override
	public void init(HardwareInterface hw) {
		
	}

	@Override
	public Object exec(String command, Object params) {
		if("OPEN".equals(command))
		{
			try {
				boolean ok  = Gdx.net.openURI((String)params);
				return ok?"OK":"KO";
			} catch (Exception e) {
				return "ERR: "+e.getMessage();
			}
		}
		return "ERR: command not found";
	}

}
