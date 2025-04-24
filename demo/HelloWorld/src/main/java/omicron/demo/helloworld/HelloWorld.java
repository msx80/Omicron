package omicron.demo.helloworld;

import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;

public class HelloWorld implements Game 
{
	
    public void init() 
    {
    }

	public boolean loop() 
	{
    	Sys.clear(0xFF); // RGBA black
    	Sys.draw(1, 70,20,0, 0, 100, 50, 0, 0);
        return true;
    }

	@Override
	public SysConfig sysConfig() 
	{
		return new SysConfig(240, 136, VirtualScreenMode.SCALED, "Hello World!", "helloworld");
	}
  
}
