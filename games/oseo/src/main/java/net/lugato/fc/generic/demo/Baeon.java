package net.lugato.fc.generic.demo;

import com.github.msx80.omicron.*;

public class Baeon extends Item {

	int ct = (int) (Math.random()*360f);
	float speed = 0.2f+(float) (Math.random()*1f);
	
	@Override
	public void update() {
		ct+=5;
		float val = (float) Math.sin( ((float)ct)*Math.PI/180f);
		pos.x += (val/2);

		pos.y -= speed;
		
		if (pos.y < -16) {
			pos.y = 220;
			pos.set((float) (Math.random()*(float)(Sys.SCREEN_WIDTH-32)+16), 220f);
		}
	}

}
