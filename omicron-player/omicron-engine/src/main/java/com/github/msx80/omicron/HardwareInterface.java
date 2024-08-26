package com.github.msx80.omicron;

import com.github.msx80.omicron.api.Sys;

public interface HardwareInterface {

	String[] startupArgs();
	Object hardware(String module, String command, Object param);
	void gamePaused();
	void gameRestored();
	Sys getSys();
	void setSys(Sys sys);
}
