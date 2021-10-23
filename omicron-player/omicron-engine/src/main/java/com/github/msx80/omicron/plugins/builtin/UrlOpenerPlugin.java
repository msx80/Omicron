package com.github.msx80.omicron.plugins.builtin;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;
import com.github.msx80.omicron.api.Sys;

public class UrlOpenerPlugin implements HardwarePlugin {

	private HardwareInterface hw;

	@Override
	public void init(Sys sys, HardwareInterface hw) {
		
		this.hw = hw;
	}

	@Override
	public Object exec(String command, Object params) {
		if("OPEN".equals(command))
		{
			try {
				hw.openUrl((String)params);
				return "OK";
			} catch (Exception e) {
				return "ERR: "+e.getMessage();
			}
		}
		return "ERR: command not found";
	}

}
