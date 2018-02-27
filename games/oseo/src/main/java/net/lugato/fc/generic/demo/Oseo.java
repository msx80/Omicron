package net.lugato.fc.generic.demo;

import com.github.msx80.omicron.*;

public class Oseo extends Item {

	public static final float SPEED = 0.3f;
	public static final float SPEEDQ = SPEED * 0.707f;

	int poppedBaloon = 0;
	
	Vector2 momentum = new Vector2();
	Vector2 speed = new Vector2();
	Controller joy;
	Vector2 appo = new Vector2();
	
	
	public Oseo(Controller joy) {
		super();
		this.joy = joy;
		
	}
	
	public void update()
	{
		if(joy.left()) flip = true;
		if(joy.right()) flip = false;
		
		momentum.set(0,0);
		switch (joy.dir()) {
		case north:
			momentum.y-=SPEED;
			
			break;
		case south:
			momentum.y+=SPEED;
			
			break;
		case east:
			momentum.x+=SPEED;
			break;
		case west:
			momentum.x-=SPEED;
			break;

		case northEast:
			momentum.y-=SPEEDQ;
			momentum.x+=SPEEDQ;
			break;
		case southEast:
			momentum.y+=SPEEDQ;
			momentum.x+=SPEEDQ;
			break;
		case northWest:
			momentum.x-=SPEEDQ;
			momentum.y-=SPEEDQ;
			break;
		case southWest:
			momentum.x-=SPEEDQ;
			momentum.y+=SPEEDQ;
			break;

		default:
			break;
		}
		
		speed.add(momentum);
		speed.scl(0.95f);
		pos.add(speed);
		
		if(pos.x > Sys.SCREEN_WIDTH-16)pos.x = Sys.SCREEN_WIDTH-16;
		if(pos.x <16) pos.x = 16;
		
		if(pos.y > Sys.SCREEN_HEIGHT-16)pos.y = Sys.SCREEN_HEIGHT-16;
		if(pos.y <16) pos.y = 16;
	}
}
