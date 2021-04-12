package %%PKG%%;


import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.TextDrawerFixed;
import com.github.msx80.omicron.basicutils.TextDrawer.Align;

public class %%MAIN%% implements Game 
{
	
	private Sys sys;
	private TextDrawerFixed td;
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        td = new TextDrawerFixed(sys, 1, 6, 6, 6);
    }

	public boolean loop() 
	{
    	sys.clear(Colors.BLACK);
    	
    	sys.draw(2, 70,20,0, 0, 100, 50, 0, 0);
    	td.print("New omicron project!", 120, 1, Align.CENTER);
        return true;
    }

	@Override
	public SysConfig sysConfig() 
	{
		return new SysConfig(240, 136, VirtualScreenMode.STRETCH_FULL, "%%NAME%%", "%%MAIN%%");
	}
  
}
