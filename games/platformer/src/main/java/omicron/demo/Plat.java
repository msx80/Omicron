package omicron.demo;

import java.util.ArrayList;
import java.util.List;

import com.github.msx80.omicron.Controller;
import com.github.msx80.omicron.Controller.Direction;
import com.github.msx80.omicron.Game;
import com.github.msx80.omicron.Image;
import com.github.msx80.omicron.Sys;

public class Plat implements Game {

	public static final int ROOM_WIDTH = 38;
	public static final int ROOM_HEIGHT = 22;
	Sys sys;
	
	Image pg;
	Image[][] tiles;
	
	int x = 10, y = 10;
	
	Room r = new Room();
	Actor p;
	List<Actor> actors = new ArrayList<Actor>();
	long lastUpdate=0;
	
	public void init(Sys sys) 
	{
		this.sys = sys;
		
		pg = sys.loadImage("pg.png");
		tiles = sys.loadSpritesheet("tiles.png", 8, 8);
		p = new Actor(7,6);
		actors.add(p);
	}

	public void update() 
	{
		Controller c = sys.controller(0);

		long now = System.currentTimeMillis();
		long delta = now-lastUpdate;
		if(delta<80) return;
		lastUpdate = now;
		
		if(c.right() && !blocked(p, r, Direction.east))
		{
			p.x++;
		}
		if(c.left() && !blocked(p, r, Direction.west))
		{
			p.x--;
		}
			
		if(c.isPressed(0))
		{
			if(blocked(p, r, Direction.south)) // only jump if something is under the feets	
			{
				p.startJump();
			}
		}
		else
		{
			p.stopJump();
		}
	
		for (Actor a : actors) {
			a.update();
			if(a.isJumping())
			{
				if(blocked(a, r, Direction.north))
				{
					a.stopJump(); // hit the head, stop the jump
				}
				else
				{
					a.y--;
				}	
			}
			else if(!blocked(a, r, Direction.south))
			{
				a.y++;
			}
			
		}
	}

	public void render() {
		
		sys.offset(8, 8);
		drawRoom(r);
		drawActors(actors);
		sys.offset(-8, -8);
	
	}

	private void drawActors(List<Actor> actors) {
		for (Actor a : actors) {
			sys.draw(pg, a.x*8, a.y*8);
			
		}
		
	}

	private void drawRoom(Room r) {
		sys.clearScreen(1f, 1f, 0.4f);
		for (int x = 0; x < Plat.ROOM_WIDTH; x++) {
			for (int y = 0; y < Plat.ROOM_HEIGHT; y++) {
				int t = r.tiles[x][y];
				
				sys.draw(tiles[t][0], x*8,y*8);
			}
		}
		
	}

	private boolean blocked(Actor a, Room r, Direction dir)
	{
		switch (dir) {
		case north:
			return r.blocked(a.x, a.y-1) || r.blocked(a.x+1, a.y-1);
		case east:
			return r.blocked(a.x+2, a.y) || r.blocked(a.x+2, a.y+1);
		case south:
			return r.blocked(a.x, a.y+2) || r.blocked(a.x+1, a.y+2);
		case west:
			return r.blocked(a.x-1, a.y) || r.blocked(a.x-1, a.y+1);
			

		default:
			throw new RuntimeException("unexpected dir");
		}
	}
	
	public void close() {		
	}
}
