package com.github.msx80.omicron;

public interface HardwareInterface {

	String[] startupArgs();
	Object hardware(String module, String command, Object param);
	void gamePaused();
	void gameRestored();
}
