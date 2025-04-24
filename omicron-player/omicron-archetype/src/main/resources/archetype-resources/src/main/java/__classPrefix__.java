#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};


import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;

public class ${classPrefix} implements Game {
	
	public static final int HEIGHT = 144;
	public static final int WIDTH = 256;
	
    public void init() 
    {
    	
    }

	public boolean loop() 
	{
		Sys.clear(255); // black
		Sys.fill(0,10,10,100,100,-16776961);  // red

        return true;
    }


	@Override
	public SysConfig sysConfig() 
	{
		return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.FILL_SIDE, "${classPrefix}", "${artifactId}");
	}

}
