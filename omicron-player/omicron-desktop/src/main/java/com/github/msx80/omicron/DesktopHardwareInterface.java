package com.github.msx80.omicron;

import com.github.msx80.omicron.api.Sys;

public class DesktopHardwareInterface implements HardwareInterface {

	
	
	private String[] args;
	PluginManager plugins = new PluginManager(this);
	private Sys sys;
	

	public DesktopHardwareInterface(String[] args) {
		this.args = args;
	}

	/*@Override
	public void openUrl(String url) throws Exception {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI(url));
		}
		else
		{
			throw new UnsupportedOperationException("Operation not supported");
		}

	}*/

	@Override
	public String[] startupArgs() {
		
		return args;
	}


	@Override
	public Object hardware(String module, String command, Object param) {
		
		return plugins.getPlugin(module).exec(command, param);
	}

	@Override
	public Sys getSys() {
		
		return sys;
	}

	@Override
	public void setSys(Sys sys) {
		this.sys = sys;
		
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

}
