package com.github.msx80.omicron.libretro.entrypoint;

import com.github.msx80.omicron.HardwareInterface;
import com.github.msx80.omicron.api.Sys;

public class NullHardwareInterface implements HardwareInterface {



	private Sys sys;



	@Override
	public String[] startupArgs() {
		return new String[0];
	}

	

	@Override
	public Object hardware(String module, String command, Object param) {
		
		return null;
	}



	@Override
	public void gamePaused() {
		
	}



	@Override
	public void gameRestored() {
		
	}


}
