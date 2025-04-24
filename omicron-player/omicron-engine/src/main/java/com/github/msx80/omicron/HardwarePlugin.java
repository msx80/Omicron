package com.github.msx80.omicron;

// experimental
public interface HardwarePlugin 
{
	void init(HardwareInterface hw);
	Object exec(String command, Object params) throws Exception;
	default void onPause() {};
	default void onResume() {};
}
