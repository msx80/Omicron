package org.github.msx80.omicron;

// experimental
public interface HardwarePlugin {
	String exec(String command, String params);
	String name();
}
