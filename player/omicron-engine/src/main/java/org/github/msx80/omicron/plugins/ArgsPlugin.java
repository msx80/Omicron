package org.github.msx80.omicron.plugins;

import org.github.msx80.omicron.HardwareInterface;
import org.github.msx80.omicron.HardwarePlugin;
import org.github.msx80.omicron.api.Sys;

public class ArgsPlugin implements HardwarePlugin {

	private HardwareInterface hw;

	@Override
	public void init(Sys sys, HardwareInterface hw) {
		this.hw = hw;

	}

	@Override
	public Object exec(String command, Object params) {
		
		return hw.startupArgs();
	}

	@Override
	public String name() {
		return "ARGS";
	}

}
