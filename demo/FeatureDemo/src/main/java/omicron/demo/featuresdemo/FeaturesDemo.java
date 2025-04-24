package omicron.demo.featuresdemo;


import java.util.Random;

import com.github.msx80.omicron.basicutils.Colors;
import com.github.msx80.omicron.basicutils.MapDrawer;
import com.github.msx80.omicron.basicutils.text.TextDrawerFixed;

import com.github.msx80.omicron.api.Controller;
import com.github.msx80.omicron.api.Game;
import com.github.msx80.omicron.api.Pointer;
import com.github.msx80.omicron.api.Sys;
import com.github.msx80.omicron.api.SysConfig;
import com.github.msx80.omicron.api.SysConfig.VirtualScreenMode;

public class FeaturesDemo implements Game {
	
	private TextDrawerFixed font = null;
	private MapDrawer mapDrawer = null;
	
	private int bgColor = Colors.from(150, 200, 255);
	Random r = new Random(10);
	
	int x=100;
	int y=10;
	int dir = 0;
	Pointer m;
	int newSurf;
	private int[][] mapData = new int[][]{
			new int[] {0,1,0,0,0,0,2,6},
			new int[] {2,4,0,5,0,0,0,13},
			new int[] {4,0,0,2,0,1,3,0},
			new int[] {0,3,0,0,4,5,0,0}
	};
	
    public void init() 
    {
        font = new TextDrawerFixed( 1, 8, 8, 6);
        mapDrawer = new MapDrawer( 8, 8, 7, mapData );
        setupSurf();
    }

	private void setupSurf() {
		newSurf = Sys.newSurface(64, 16);
		
        for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 16; y++) {
				Sys.fill(newSurf, x, y, 1, 1, r.nextInt());
			}
		}
	}

	private void testRot(int x, int y) {
		font.print("Rot & flip:", x, y);
		y+=10;
		for (int i = 0; i < 4; i++) {
			Sys.draw(2, x+i*18,y, 0,0, 16, 16, i, 0 );
			Sys.draw(2, x+i*18,y+20, 0,0, 16, 16, i, 1 );
			Sys.draw(2, x+i*18,y+40, 0,0, 16, 16, i, 2 );
			Sys.draw(2, x+i*18,y+60, 0,0, 16, 16, i, 3 );
		}
		//sys.draw(2, 10,10, 32,32, 16, 16);
	}
    

	
    public void render() 
    {
  
    	Sys.clear(bgColor);
    	
    	font.print("Maps:", 140, 91);
    	mapDrawer.draw(3, 140, 100, 0, 0, 8, 4);
    	
    	// keyboard controlled sprite
        Sys.draw(2, x, y, 0, 32, 16, 16,0,dir);
        
    	
        
        Sys.color(Colors.RED);
        font.print("FPS: "+Sys.fps(), 10, 10);
        
        Sys.color(Colors.WHITE);
   
        
        font.print("Pointer: ", 10, 20);
        font.print(""+m, 10, 28);
        Controller c = Sys.controllers()[0];
        font.print("Controller: ", 10, 37);
        font.print(""+c, 10, 37+8);
              
        
        font.print("New surf:", 10, 55);
        Sys.draw(newSurf, 10, 65, 0,0, 64, 16, 0, 0);
        
        
        testRot(140, 2);
    	
        testTrans(10,100);
        
        
        
        // mouse pointer
        Sys.draw(2, m.x(),m.y(),64,0,8,8,0,0);
        
        
        
        
    }

    private void testTrans(int x, int y) {
    	font.print("Color & alpha:", x, y);
    	y+=10;
    	Sys.color(Colors.WHITE);
    	Sys.draw(2, x, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.RED);
    	Sys.draw(2, x+16, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.GREEN);
    	Sys.draw(2, x+32, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.BLUE);
    	Sys.draw(2, x+48, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.from(255, 255, 255, 180));
    	Sys.draw(2, x+64, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.from(255, 255, 255, 100));
    	Sys.draw(2, x+80, y, 0, 16, 16, 16,0,0);
    	Sys.color(Colors.from(255, 255, 255, 30));
    	Sys.draw(2, x+96, y, 0, 16, 16, 16,0,0);
    	
    	
    	Sys.color(Colors.WHITE);
		
	}

	public boolean loop() {
			
        Controller c = Sys.controllers()[0];
        if (c.up()) y--;
        if (c.down()) y++;
        if (c.left()) { x--; dir = 1; }
        if (c.right()) { x++; dir = 0; }
        
        
        m = Sys.pointers()[0];
        if(m.btnp(0)) {
        	Sys.sound(1, 1f, r.nextFloat()*1.5f+0.5f);
        }
        
        render();
        
        return true;
    }

	@Override
	public SysConfig sysConfig() {
		return new SysConfig(240, 136, VirtualScreenMode.SCALED, "Hello World!", "helloworld");
	}
  
}
