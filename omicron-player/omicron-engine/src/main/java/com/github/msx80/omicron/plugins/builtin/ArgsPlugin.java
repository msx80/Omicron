package com.github.msx80.omicron.plugins.builtin;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.HardwarePlugin;

public class ArgsPlugin implements HardwarePlugin 
{

	private HardwareInterface hw;

	@Override
	public void init(HardwareInterface hw) {
		this.hw = hw;
	}

	@Override
	public Object exec(String command, Object params) 
	{
		if ("GET".equals(command)) {
			
			return hw.startupArgs();
		}
		throw new UnsupportedOperationException("unexpected command "+command);
	}

}
