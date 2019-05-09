package org.github.msx80.omicron;


import java.util.stream.IntStream;

import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.ScreenConfig;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class HelloWorld implements Game {
	
	private Sys sys;
	private int tic = 0;
	private TextDrawer font = null;

	private int bgColor = Colors.from(200, 200, 255);
	
	int x=100;
	int y=50;
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        font = new TextDrawer(sys, 1, 8, 8, 6);
    }

    public void render() 
    {
    	sys.clear(bgColor);
    	
        font.print("Hello world!", 10, 10);
        sys.color(Colors.RED);
        font.print("FPS: "+sys.fps(), 10, 40);
        sys.color(Colors.WHITE);
        
        Mouse m = sys.mouse();
        font.print("Mouse: "+m.x+" "+m.y+" "+m.btn[0], 10, 50);
      
        // mouse pointer
        sys.draw(2, m.x,m.y,0,24,8,8);
        
        // keyboard controlled sprite
        sys.draw(2, x, y, 0, 32, 8, 8);
        
        
        sys.draw(2, tic % 100, 50, 0,0, 24, 24);
    	
        
    }

    public boolean update() {
			
        tic++;
        
        Controller c = sys.controllers()[0];
        if (c.up) y--;
        if (c.down) y++;
        if (c.left) x--;
        if (c.right) x++;
     
        return true;
    }

	@Override
	public ScreenConfig screenConfig() {
		return new ScreenConfig(240, 136, "Hello World!");
	}
  
}
