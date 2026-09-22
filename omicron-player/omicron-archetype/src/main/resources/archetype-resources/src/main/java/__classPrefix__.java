#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};


import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;
import com.github.msx80.omicron.basicutils.text.TextDrawer;
import com.github.msx80.omicron.basicutils.text.TextDrawerVariable;
import com.github.msx80.omicron.api.Controller;
import com.github.msx80.omicron.basicutils.Colors;

public class ${classPrefix} implements Game {

  public static final int HEIGHT = 144;
  public static final int WIDTH = 256;

  private TextDrawer textDrawer;
  private int x = 50;
  private int y = 50;

  public void init() 
  {
    textDrawer = TextDrawerVariable.DEFAULT;
  }

	public boolean loop() 
	{
	  Controller c = Sys.controllers()[0];
	  
	  if(c.up()) y--;
	  if(c.down()) y++;
	  if(c.left()) x--;
	  if(c.right()) x++;
	  
	  // rendering
	  
    Sys.clear( Colors.from(30, 60, 90) );
    Sys.fill( 0,5,5,WIDTH-10,20, Colors.RED ); 
    textDrawer.print("Hello world! Move the robot with cursor keys :)", 10, 10);
    
    // cute robot sprite by https://wrabitart.itch.io/
    Sys.draw(1, x, y, 0, 0, 16, 16,  0, 0);
    
    return true;
  }

  @Override
  public SysConfig sysConfig() 
  {
    return new SysConfig(WIDTH, HEIGHT, VirtualScreenMode.FILL_SIDE, "${classPrefix}", "${artifactId}");
  }

}
