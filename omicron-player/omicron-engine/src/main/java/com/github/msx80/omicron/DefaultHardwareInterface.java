package com.github.msx80.omicron;

public class DefaultHardwareInterface implements HardwareInterface {

	private String[] args;
	PluginManager plugins = new PluginManager(this);
		

	public DefaultHardwareInterface(String[] args) {
		this.args = args;
	}

	@Override
	public String[] startupArgs() {
		
		return args;
	}


	@Override
	public Object hardware(String module, String command, Object param) {
		
		try {
			return plugins.getPlugin(module).exec(command, param);
		} catch (Exception e) {
			throw new RuntimeException("Plugin gave an exception: "+e.getMessage(), e);
		}
	}

	@Override
	public void gamePaused() {
		for(HardwarePlugin p : plugins.getPlugins())
		{
			p.onPause();
		}
		
	}

	@Override
	public void gameRestored() {
		for(HardwarePlugin p : plugins.getPlugins())
		{
			p.onResume();
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends HardwarePlugin> loadPluginClass(String module) throws Exception {
		
		return  (Class<? extends HardwarePlugin>) this.getClass().getClassLoader().loadClass(module);
	}

}
