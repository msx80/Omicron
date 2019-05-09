Omicron
======
Omicron is an open source Game Engine/Fantasy Console for Java based on LibGDX. Write your retro games with the simplest possible API and either package it as a cartridge, or include the runtime in your own distribution!

How can I try it?
-----------------

1. Build the omicron-api, omicron-engine  project (mvn clean install)
2. Build the HelloWorld cartridge (or your own) (gradlew build)
3. Run the cartridge (java -jar omicron.jar Helloworld.omicron)

The API
-------

Here's Omicron minimalistic and self-explaining API:

```java
package org.github.msx80.omicron.api;

public interface Sys {
	
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h);
    void draw(int sheetNum, int x, int y, int srcx, int srcy, int w, int h, int rotate);
    
    int newSurface(int w, int h);
    
    int getPix(int sheetNum, int x, int y);
    void setPix(int sheetNum, int x, int y, int color);
    
	void offset(int x, int y);

	void clear(int color);

	void color(int color);
	
	int fps();
	
	Mouse mouse();

	Controller[] controllers();
}

```

Hello World
-----------

Here's the minimal Hello World example, a controlled sprite with a background:

```java
package org.github.msx80.omicron;


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

```