package com.github.msx80.omicron;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {
	Map<String, HardwarePlugin> plugins = new HashMap<String, HardwarePlugin>();
	private HardwareInterface hw;

	public PluginManager(HardwareInterface hw) {

		this.hw = hw;
		
	}
	public HardwarePlugin getPlugin(String module) 
	{
		HardwarePlugin e = plugins.get(module);
		if(e==null) e = initPlugin(module);
		return e;
	}
	private HardwarePlugin initPlugin(String module) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends HardwarePlugin> cc = (Class<? extends HardwarePlugin>) this.getClass().getClassLoader().loadClass(module);
			HardwarePlugin p = cc.newInstance();
			p.init(hw);
			plugins.put(module, p);
			return p;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load plugin "+module, e);
		}
	}
	public Collection<HardwarePlugin> getPlugins()
	{
		return plugins.values();
	}
}
