package org.github.msx80.omicron;


import java.util.stream.IntStream;

import org.github.msx80.omicron.api.Controller;
import org.github.msx80.omicron.api.Game;
import org.github.msx80.omicron.api.Mouse;
import org.github.msx80.omicron.api.ScreenConfig;
import org.github.msx80.omicron.api.Sys;
import org.github.msx80.omicron.basicutils.Colors;
import org.github.msx80.omicron.basicutils.TextDrawer;

public class DemoGame implements Game {
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 136;
	   
	
	private Sys sys;
	private int ii = 0;
	private TextDrawer msxFont = null;
	private TextDrawer tic80Font = null;

	private int bgColor = Colors.from(82, 191, 191);
	
	int x=100;
	int y=50;
	
    public void init(final Sys sys) 
    {
        this.sys = sys;
        msxFont = new TextDrawer(sys, 1, 8, 8, 6);
        tic80Font = new TextDrawer(sys, 6, 6, 6, 6);
    }

    public void render() 
    {
    	sys.clear(bgColor);

    	//sys.offset(30, 0);
    	
    	
    	
    	for (int i = 0; i < 10; i++) 
    	{
	    	for (int x = 0; x < WIDTH/8-5; x++) 
	    	{
		    	for (int y = 0; y < HEIGHT/8; y++) 
		    	{
		    			sys.draw(2, x*8, y*8, 8,32,8,8);
		    	}
			}
    	}
    	sys.draw(7, 0, 0, 0, 0, 400, 255);
        msxFont.print("ciaoo :P", 10, 0);
        msxFont.print("FPS: "+sys.fps(), 10, 40);
        sys.color(Colors.GREEN);
        tic80Font.print("Hello there Tic80 Font :)", 80, 40);
        sys.color(Colors.WHITE);
        tic80Font.print("FPS: "+sys.fps(), 140, 46);
        
        msxFont.print(sys.dbg(), 10, 70);
        
        Mouse m = sys.mouse();
        msxFont.print("Mouse: "+m.x+" "+m.y+" "+m.btn[0], 10, 50);
        
        Controller c = sys.controllers()[0];
        msxFont.print("Ctrl: "+c.up+" "+c.down+" "+c.left+" "+c.right+" "+c.btn[0]+" "+c.btn[1]+" "+c.btn[2], 10, 60);
    	
        testRotate();
        
        // mouse pointer
        sys.draw(2, m.x,m.y,0,24,8,8);
        
        sys.draw(2, x,y,256-32,0,32,32);
        
        // keyboard controlled sprite
        sys.draw(2, x, y, 0, 32, 8, 8);
        
        IntStream.range(1, 5).forEach(i -> {
    		sys.offset(i*5, 0);
    		sys.draw(2, (ii+i)  % 100, 20+i*30, 0,0,24,24);
    		sys.color(Colors.from(10, 255, 10, 170));
        });
        
    	sys.offset(0, 0);
    	sys.color(Colors.BLACK);
    	msxFont.print("JAVA8baby", 10, 10);
        
    }

	private void testRotate() {
		sys.draw(2, 20,20, 0,96,16,16);
        sys.draw(2, 20+16,20, 0,96,16,16, 0);
        sys.draw(2, 20+32,20, 0,96,16,16, 90);
        sys.draw(2, 20+32+16,20, 0,96,16,16, 180);
        sys.draw(2, 20+64,20, 0,96,16,16, 270);
        sys.draw(2, 20+64+16,20, 0,96,16,16, ii%360);
        

        sys.draw(2, 10,100, 0,112,24,24);
        sys.draw(2, 10,124, 0,112,24,24,0);
        sys.draw(2, 10+24,100, 0,112,24,24, 90);
        sys.draw(2, 10+48,100, 0,112,24,24, 180);
        sys.draw(2, 10+72,100, 0,112,24,24, 270);
        sys.draw(2, 10+96,100, 0,112,24,24, ii%360);
	}

    public boolean update() {
			
        ii++;
        
        Controller c = sys.controllers()[0];
        if (c.up) y--;
        if (c.down) y++;
        if (c.left) x--;
        if (c.right) x++;
     
        return true;
    }

	@Override
	public ScreenConfig screenConfig() {
		return new ScreenConfig(WIDTH, HEIGHT, "Very nice test");
	}
    
  /*
    public static void main(String[] args)
    {
    	DesktopLauncher.launch(new DemoGame());
    }
*/    
}
