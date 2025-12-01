package com.github.msx80.omicron;

public interface HardwareInterface {

	String[] startupArgs();
	Object hardware(String module, String command, Object param);
	void gamePaused();
	void gameRestored();
	
	/**
	 * Load the plugin class. If possible, the plugin should be loaded in the omicron player classloader to get all privileges, otherwise
	 * it will run with the same privileges of the cartridge.
	 * For system with limitations (TeaVM), any classloader can be used
	 * @param module
	 * @return
	 * @throws Exception 
	 */
	Class<? extends HardwarePlugin> loadPluginClass(String module) throws Exception;
}
