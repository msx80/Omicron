package org.github.msx80.omicron;

import org.github.msx80.omicron.api.Sys;

// experimental
public interface HardwarePlugin {
	void init(Sys sys, HardwareInterface hw);
	Object exec(String command, Object params);
	String name();
}
