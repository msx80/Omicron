package org.github.msx80.omicron.helloworld;


import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.api.SysConfig;
import org.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import org.github.msx80.omicron.basicutils.Colors;

public class HelloWorld implements Game 
{
	
	private Sys sys;
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
    }

	public boolean loop() 
	{
    	sys.clear(Colors.BLACK);
    	sys.draw(1, 70,20,0, 0, 100, 50, 0, 0);
        return true;
    }

	@Override
	public SysConfig sysConfig() 
	{
		return new SysConfig(240, 136, VirtualScreenMode.SCALED, "Hello World!", "helloworld");
	}
  
}
