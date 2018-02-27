package omicron.demo;

import com.github.msx80.omicron.Controller;
import com.github.msx80.omicron.Game;
import com.github.msx80.omicron.Image;
import com.github.msx80.omicron.Sys;

public class HelloWorld implements Game {

	Image background, sprite;
	int x = 100, y = 100;
	Sys sys;
	
	public void init(Sys sys) 
	{
		this.sys = sys;
		
		background = sys.loadImage("stars.png");
		sprite = sys.loadImage("ufo.png");
	}

	public void update() 
	{
		Controller c = sys.controller(0);
		y += c.up() ? -2 : c.down() ? 2 : 0;
		x += c.left() ? -2 : c.right() ? 2 : 0;
	}

	public void render() {
		sys.draw(background, 0, 0);
		sys.draw(sprite, x, y);
		sys.color(0.5f, 1, 0, 1);
		sys.write(null, 5, 5, "Very simple movement demo. Have fun!");
		
		String s = "There are "+sys.availableControllers()+" controllers: ";
		for (int i = 0; i < sys.availableControllers(); i++) {
			s+=sys.controller(i).name()+" ";
		}
		sys.write(null, 0, Sys.SCREEN_HEIGHT-8, s);
	}

	public void close() {		
	}
}
